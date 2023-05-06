package me.retamrovec.economy;

import me.retamrovec.economy.api.EconomyAPI;
import me.retamrovec.economy.api.Vault;
import me.retamrovec.economy.database.Database;
import me.retamrovec.economy.listeners.ConnectListener;
import me.retamrovec.economy.modules.EconomyInterfaceAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Economy extends JavaPlugin {

	private EconomyAPI api;

	@Override
	public void onEnable() {
		saveDefaultConfig();

		Database database = new Database(getConfig());
		database.connect();
		ServicesManager sm = getServer().getServicesManager();
		api = new EconomyAPI(this, database);
		Vault vault = new Vault(api);
		sm.register(EconomyInterfaceAPI.class, api, this, ServicePriority.High);
		sm.register(net.milkbowl.vault.economy.Economy.class, vault, this, ServicePriority.High);
		api.load(vault);

		Bukkit.getPluginManager().registerEvents(new ConnectListener(api), this);
		Bukkit.getOnlinePlayers().forEach(api::addPlayer);
	}

	@Override
	public void onDisable() {
		Bukkit.getOnlinePlayers().forEach(player -> api.removePlayer(player, false));
	}
}
