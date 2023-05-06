package me.retamrovec.economy;

import me.retamrovec.economy.api.EconomyAPI;
import me.retamrovec.economy.api.Vault;
import me.retamrovec.economy.database.Database;
import me.retamrovec.economy.modules.EconomyInterfaceAPI;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Economy extends JavaPlugin {

	private static EconomyAPI api;
	private Database database;

	@Override
	public void onEnable() {
		saveDefaultConfig();

		database = new Database(getConfig());
		database.connect();
		Vault vault = new Vault(api);
		ServicesManager sm = getServer().getServicesManager();
		api = new EconomyAPI(this, vault, database);
		sm.register(EconomyInterfaceAPI.class, api, this, ServicePriority.High);
		sm.register(net.milkbowl.vault.economy.Economy.class, vault, this, ServicePriority.High);
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}

	public static EconomyAPI getAPI() {
		return api;
	}
}
