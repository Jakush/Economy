package me.retamrovec.economy;

import me.retamrovec.economy.api.EconomyAPI;
import me.retamrovec.economy.api.Vault;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Economy extends JavaPlugin {

	private static EconomyAPI api;
	private Vault vault;

	@Override
	public void onEnable() {
		vault = new Vault();
		if (!vault.setupEconomy()) {
			Bukkit.getLogger().severe("No Vault dependecy found!");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}

		api = new EconomyAPI(vault);
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}

	public static EconomyAPI getAPI() {
		return api;
	}
}
