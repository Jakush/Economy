package me.retamrovec.economy.api;

import me.retamrovec.economy.Economy;
import me.retamrovec.economy.modules.EconomyPlayer;
import org.bukkit.Bukkit;

import java.util.UUID;

public class EconomyAPI {

	private final Vault vault;
	public EconomyAPI(Vault vault) {
		this.vault = vault;
	}

	public EconomyPlayer getPlayer(UUID uuid) {
		return new EconomyPlayer(uuid, vault);
	}
}
