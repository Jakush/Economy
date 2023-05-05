package me.retamrovec.economy.modules;

import me.retamrovec.economy.api.Vault;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class EconomyPlayer {

	private final UUID uuid;
	private final OfflinePlayer player;
	private final Vault vault;

	public EconomyPlayer(UUID uuid, Vault vault) {
		this.uuid = uuid;
		this.vault = vault;
		this.player = Bukkit.getOfflinePlayer(uuid);
	}

	public UUID getUniqueId() {
		return uuid;
	}

	public Double getBalance() {
		return vault.getEconomy().getBalance(player);
	}

	// Adding money to player
	public Boolean depositMoney(Double amount) {
		Bukkit.getLogger().info("oldAmount " + getBalance());
		EconomyResponse response = vault.getEconomy().depositPlayer(player, amount);
		Bukkit.getLogger().info("isSuccess " + response.transactionSuccess());
		if (response.transactionSuccess()) {
			Bukkit.getLogger().info("newAmount " + getBalance());
		}
		return response.transactionSuccess();
	}

	// Removing money from player
	public Boolean withdrawMoney(Double amount) {
		Bukkit.getLogger().info("oldAmount " + getBalance());
		EconomyResponse response = vault.getEconomy().withdrawPlayer(player, amount);
		Bukkit.getLogger().info("isSuccess " + response.transactionSuccess());
		if (response.transactionSuccess()) {
			Bukkit.getLogger().info("newAmount " + getBalance());
		}
		return response.transactionSuccess();
	}
}
