package me.retamrovec.economy.api;

import me.retamrovec.economy.database.Database;
import me.retamrovec.economy.modules.EconomyInterfaceAPI;
import me.retamrovec.economy.modules.EconomyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class EconomyAPI implements EconomyInterfaceAPI {

	private final Vault vault;
	private final HashMap<UUID, EconomyPlayer> economyPlayers;
	private final Database database;
	private final JavaPlugin plugin;

	public EconomyAPI(JavaPlugin plugin, Vault vault, Database database) {
		this.vault = vault;
		this.economyPlayers = new HashMap<>();
		this.database = database;
		this.plugin = plugin;
	}

	@Override
	public void addPlayer(UUID uuid, EconomyPlayer player) {
		economyPlayers.put(uuid, player);
	}

	@Override
	public void removePlayer(UUID uuid) {
		economyPlayers.remove(uuid);
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
		AtomicReference<EconomyPlayer> playerAtomicReference = new AtomicReference<>();
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
			try (PreparedStatement ps = database.prepareStatement("SELECT * WHERE player = (?) LIMIT 1;")) {
				ps.setString(1, player.getName());
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					playerAtomicReference.set(new EconomyPlayer(rs.getString("player"), rs.getDouble("balance")));
				}
				else {
					boolean success = createPlayerAccountAsync(player);
					if (success) {
						playerAtomicReference.set(loadPlayerAccountAsync(player));
					}
					else {
						Bukkit.getLogger().severe("[Economy] Problem was found while loading player account!");
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		return playerAtomicReference.get();
	}

	@Override
	public boolean createPlayerAccountAsync(OfflinePlayer player) {
		AtomicBoolean state = new AtomicBoolean(false);
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> state.set(vault.createPlayerAccount(player)));
		return state.get();
	}

	@Override
	public boolean depositPlayerAsync(OfflinePlayer player, Double amount) {
		AtomicBoolean state = new AtomicBoolean(false);
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> state.set(vault.depositPlayer(player, amount).transactionSuccess()));
		return state.get();
	}

	@Override
	public boolean withdrawPlayerAsync(OfflinePlayer player, Double amount) {
		AtomicBoolean state = new AtomicBoolean(false);
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> state.set(vault.withdrawPlayer(player, amount).transactionSuccess()));
		return state.get();
	}
}
