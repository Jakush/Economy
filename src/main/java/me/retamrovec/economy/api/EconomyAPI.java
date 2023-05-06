package me.retamrovec.economy.api;

import me.retamrovec.economy.database.Database;
import me.retamrovec.economy.modules.EconomyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class EconomyAPI {

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

	public void addPlayer(UUID uuid) {
		OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
		Double balance = vault.getBalance(player);
		EconomyPlayer economyPlayer = new EconomyPlayer(player.getName(), balance);
		economyPlayers.put(uuid, economyPlayer);
	}

	public EconomyPlayer getPlayer(UUID uuid) {
		return economyPlayers.get(uuid);
	}

	public Database getDatabase() {
		return database;
	}

	public JavaPlugin getPlugin() {
		return plugin;
	}
}
