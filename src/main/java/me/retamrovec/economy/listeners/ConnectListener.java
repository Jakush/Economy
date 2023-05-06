package me.retamrovec.economy.listeners;

import me.retamrovec.economy.api.EconomyAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class ConnectListener implements Listener {

	private final EconomyAPI api;

	public ConnectListener(EconomyAPI api) {
		this.api = api;
	}

	@EventHandler
	public void onJoin(@NotNull PlayerJoinEvent e) {
		Player player = e.getPlayer();
		api.addPlayer(player.getUniqueId(), api.loadPlayerAccountAsync(player));
	}

	@EventHandler
	public void onLeave(@NotNull PlayerQuitEvent e) {
		Player player = e.getPlayer();
		api.removePlayer(player.getUniqueId());
	}
}
