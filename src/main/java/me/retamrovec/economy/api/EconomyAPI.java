package me.retamrovec.economy.api;

import me.retamrovec.economy.Economy;
import me.retamrovec.economy.database.Database;
import me.retamrovec.economy.modules.EconomyInterfaceAPI;
import me.retamrovec.economy.modules.EconomyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class EconomyAPI implements EconomyInterfaceAPI {

	private Vault vault;
	private final HashMap<UUID, EconomyPlayer> economyPlayers;
	private final Database database;
	private final Economy economy;

	public EconomyAPI(Economy economy, Database database) {
		this.economyPlayers = new HashMap<>();
		this.database = database;
		this.economy = economy;
	}

	public void load(Vault vault) {
		this.vault = vault;
	}

	@Override
	public void addPlayer(@NotNull Player player) {
		economyPlayers.put(player.getUniqueId(), loadPlayerAccountAsync(player));
	}

	@Override
	public void removePlayer(@NotNull Player player, boolean async) {
		Runnable runnable = () -> {
			EconomyPlayer economyPlayer = economyPlayers.get(player.getUniqueId());
			try (PreparedStatement ps = database.prepareStatement("UPDATE Econ SET balance = (?) WHERE player = (?);")) {
				ps.setDouble(1, economyPlayer.getBalance());
				ps.setString(2, player.getName());
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			economyPlayers.remove(player.getUniqueId());
		};
		if (async) {
			Bukkit.getScheduler().runTaskAsynchronously(economy, runnable);
			return;
		}
		runnable.run();
	}

	@Override
	public EconomyPlayer getPlayer(UUID uuid) {
		return economyPlayers.get(uuid);
	}

	@Override
	public Database getDatabase() {
		return database;
	}


	/*
	ASync methods -
	these methods should be used instead of normal ones (sync)
	async means, it will be run on another thread, and it won't block the main thread (game one)
	*/

	@Override
	public EconomyPlayer loadPlayerAccountAsync(OfflinePlayer player) {
		CompletableFuture<EconomyPlayer> completableFuture = CompletableFuture.supplyAsync(() -> {
			try (PreparedStatement ps = database.prepareStatement("SELECT * FROM Econ WHERE player = (?) LIMIT 1;")) {
				ps.setString(1, player.getName());
				ResultSet rs = ps.executeQuery();

				if (rs.next()) {
					return new EconomyPlayer(rs.getString("player"), rs.getDouble("balance"));
				}
				else {
					boolean success = createPlayerAccountAsync(player);
					if (success) {
						return loadPlayerAccountAsync(player);
					}
					else {
						Bukkit.getLogger().severe("[Economy] Problem was found while loading player account!");
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		});
		try {
			return completableFuture.get(8, TimeUnit.SECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean createPlayerAccountAsync(OfflinePlayer player) {
		CompletableFuture<Boolean> completableFuture = CompletableFuture.supplyAsync(() -> vault.createPlayerAccount(player));
		try {
			return completableFuture.get(8, TimeUnit.SECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Deposits amount from player
	 * <h4>DON'T EVER USE THIS METHOD AS YOU SHOULDN'T MANIPULATE WITH DATABASE WHILE RUNNING SERVER BECAUSE OF PERFORMANCE ISSUES </h4>
	 *
	 * @param player Player's account
	 * @param amount Amount to deposit
	 * @return Returns its success
	 */
	@Override
	public boolean depositPlayerAsync(OfflinePlayer player, Double amount) {
		CompletableFuture<Boolean> completableFuture = CompletableFuture.supplyAsync(() -> vault.depositPlayer(player, amount).transactionSuccess());
		try {
			return completableFuture.get(8, TimeUnit.SECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Withdraw amount from player
	 * <h4>DON'T EVER USE THIS METHOD AS YOU SHOULDN'T MANIPULATE WITH DATABASE WHILE RUNNING SERVER BECAUSE OF PERFORMANCE ISSUES </h4>
	 *
	 * @param player Player's account
	 * @param amount Amount to withdraw
	 * @return Returns its success
	 */
	@Override
	public boolean withdrawPlayerAsync(OfflinePlayer player, Double amount) {
		CompletableFuture<Boolean> completableFuture = CompletableFuture.supplyAsync(() -> vault.withdrawPlayer(player, amount).transactionSuccess());
		try {
			return completableFuture.get(8, TimeUnit.SECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			throw new RuntimeException(e);
		}
	}
}
