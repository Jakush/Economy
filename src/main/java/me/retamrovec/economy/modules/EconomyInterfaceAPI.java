package me.retamrovec.economy.modules;

import me.retamrovec.economy.database.Database;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public interface EconomyInterfaceAPI {

	void addPlayer(UUID uuid, EconomyPlayer player);
	void removePlayer(UUID uuid);
	EconomyPlayer getPlayer(UUID uuid);
	Database getDatabase();

	/* ASync methods */
	EconomyPlayer loadPlayerAccountAsync(OfflinePlayer player);
	boolean createPlayerAccountAsync(OfflinePlayer player);
	boolean depositPlayerAsync(OfflinePlayer player, Double amount);
	boolean withdrawPlayerAsync(OfflinePlayer player, Double amount);

}
