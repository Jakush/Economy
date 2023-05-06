package me.retamrovec.economy.api;

import me.retamrovec.economy.modules.EconomyPlayer;
import me.retamrovec.economy.modules.EconomyProperties;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecated")
public class Vault implements Economy {

	private final EconomyAPI api;

	public Vault(EconomyAPI api) {
		this.api = api;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getName() {
		return EconomyProperties.CURRENCY;
	}

	@Override
	public boolean hasBankSupport() {
		return EconomyProperties.HAS_BANK_SUPPORT;
	}

	@Override
	public int fractionalDigits() {
		return EconomyProperties.FRACTIONAL_DIGITS;
	}

	@Override
	public String format(double amount) {
		return null;
	}

	@Override
	public String currencyNamePlural() {
		return EconomyProperties.CURRENCY;
	}

	@Override
	public String currencyNameSingular() {
		return EconomyProperties.CURRENCY;
	}

	@Override
	public boolean hasAccount(String playerName) {
		return api.getPlayer(Bukkit.getPlayerUniqueId(playerName)) != null;
	}

	/**
	 * Returns state
	 * @param player to check in the world
	 * @return Returns its success
	 */
	@Override
	public boolean hasAccount(@NotNull OfflinePlayer player) {
		return api.getPlayer(player.getUniqueId()) != null;
	}

	/**
	 * Returns state
	 * @param playerName to check in the world
	 * @param worldName world-specific account - IGNORED
	 * @deprecated Deprecated because of not performance friendly method
	 * @return Returns its success
	 */
	@Deprecated
	@Override
	public boolean hasAccount(String playerName, String worldName) {
		return api.getPlayer(Bukkit.getPlayerUniqueId(playerName)) != null;
	}

	/**
	 * Returns state
	 * @param player to check in the world
	 * @param worldName world-specific account - IGNORED
	 * @return Returns its success
	 */
	@Override
	public boolean hasAccount(@NotNull OfflinePlayer player, String worldName) {
		return api.getPlayer(player.getUniqueId()) != null;
	}

	/**
	 * Gets player's balance
	 * @param playerName player's name
	 * @deprecated Deprecated because of not performance friendly method
	 * @return Returns balance
	 */
	@Deprecated
	@Override
	public double getBalance(String playerName) {
		EconomyPlayer economyPlayer = api.getPlayer(Bukkit.getPlayerUniqueId(playerName));
		return economyPlayer == null ? 0 : economyPlayer.getBalance();
	}

	/**
	 * Gets player's balance
	 * @param player player
	 * @return Returns balance
	 */
	@Override
	public double getBalance(@NotNull OfflinePlayer player) {
		EconomyPlayer economyPlayer = api.getPlayer(player.getUniqueId());
		return economyPlayer == null ? 0 : economyPlayer.getBalance();
	}

	/**
	 * Gets balance
	 * @param playerName player's name
	 * @param world world to check
	 * @deprecated Deprecated because of not performance friendly method
	 * @return Returns balance
	 */
	@Deprecated
	@Override
	public double getBalance(String playerName, String world) {
		EconomyPlayer economyPlayer = api.getPlayer(Bukkit.getPlayerUniqueId(playerName));
		return economyPlayer == null ? 0 : economyPlayer.getBalance();
	}

	/**
	 * Gets player's balance
	 * @param player player
	 * @param world world to check
	 * @return Returns balance
	 */
	@Override
	public double getBalance(@NotNull OfflinePlayer player, String world) {
		EconomyPlayer economyPlayer = api.getPlayer(player.getUniqueId());
		return economyPlayer == null ? 0 : economyPlayer.getBalance();
	}

	@Override
	public boolean has(String playerName, double amount) {
		return getBalance(playerName) >= amount;
	}

	@Override
	public boolean has(OfflinePlayer player, double amount) {
		return getBalance(player) >= amount;
	}

	@Override
	public boolean has(String playerName, String worldName, double amount) {
		return getBalance(playerName, worldName) >= amount;
	}

	@Override
	public boolean has(OfflinePlayer player, String worldName, double amount) {
		return getBalance(player, worldName) >= amount;
	}

	@Override
	public EconomyResponse withdrawPlayer(String playerName, double amount) {
		return withdrawPlayer(Bukkit.getOfflinePlayer(playerName), amount);
	}

	@Override
	public EconomyResponse withdrawPlayer(@NotNull OfflinePlayer player, double amount) {
		EconomyPlayer economyPlayer = api.getPlayer(player.getUniqueId());
		if (economyPlayer == null) return new EconomyResponse(0, amount, EconomyResponse.ResponseType.FAILURE, "failure");
		economyPlayer.setBalance(economyPlayer.getBalance() - amount);
		return new EconomyResponse(economyPlayer.getBalance(), amount, EconomyResponse.ResponseType.SUCCESS, "success");
	}

	@Override
	public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
		return withdrawPlayer(playerName, amount);
	}

	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
		return withdrawPlayer(player, amount);
	}

	@Override
	public EconomyResponse depositPlayer(String playerName, double amount) {
		return depositPlayer(Bukkit.getOfflinePlayer(playerName), amount);
	}

	@Override
	public EconomyResponse depositPlayer(@NotNull OfflinePlayer player, double amount) {
		EconomyPlayer economyPlayer = api.getPlayer(player.getUniqueId());
		if (economyPlayer == null) return new EconomyResponse(0, amount, EconomyResponse.ResponseType.FAILURE, "failure");
		economyPlayer.setBalance(economyPlayer.getBalance() + amount);
		return new EconomyResponse(economyPlayer.getBalance(), amount, EconomyResponse.ResponseType.SUCCESS, "success");
	}

	@Override
	public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
		return depositPlayer(playerName, amount);
	}

	@Override
	public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
		return depositPlayer(player, amount);
	}

	@Override
	public EconomyResponse createBank(String name, String player) {
		return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "not implemented");
	}

	@Override
	public EconomyResponse createBank(String name, OfflinePlayer player) {
		return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "not implemented");
	}

	@Override
	public EconomyResponse deleteBank(String name) {
		return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "not implemented");
	}

	@Override
	public EconomyResponse bankBalance(String name) {
		return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "not implemented");
	}

	@Override
	public EconomyResponse bankHas(String name, double amount) {
		return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "not implemented");
	}

	@Override
	public EconomyResponse bankWithdraw(String name, double amount) {
		return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "not implemented");
	}

	@Override
	public EconomyResponse bankDeposit(String name, double amount) {
		return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "not implemented");
	}

	@Override
	public EconomyResponse isBankOwner(String name, String playerName) {
		return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "not implemented");
	}

	@Override
	public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
		return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "not implemented");
	}

	@Override
	public EconomyResponse isBankMember(String name, String playerName) {
		return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "not implemented");
	}

	@Override
	public EconomyResponse isBankMember(String name, OfflinePlayer player) {
		return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "not implemented");
	}

	@Override
	public List<String> getBanks() {
		return new ArrayList<>();
	}

	/**
	 * @Deprecated Use async method instead
	 */
	@Deprecated
	@Override
	public boolean createPlayerAccount(String playerName) {
		return createPlayerAccount(Bukkit.getOfflinePlayer(playerName));
	}

	/**
	 * @Deprecated Use async method instead
	 */
	@Deprecated
	@Override
	public boolean createPlayerAccount(@NotNull OfflinePlayer player) {
		try (PreparedStatement ps = api.getDatabase().prepareStatement("SELECT * FROM Econ WHERE player = (?);")) {
			ps.setString(1, player.getName());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) return false;
			try (PreparedStatement ps1 = api.getDatabase().getConnection().prepareStatement("INSERT INTO Econ VALUES (?,?);")) {
				ps1.setString(1, player.getName());
				ps1.setDouble(2, 0);
				ps1.executeUpdate();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * @Deprecated Use async method instead
	 */
	@Deprecated
	@Override
	public boolean createPlayerAccount(String playerName, String worldName) {
		return createPlayerAccount(Bukkit.getOfflinePlayer(playerName));
	}

	/**
	 * @Deprecated Use async method instead
	 */
	@Deprecated
	@Override
	public boolean createPlayerAccount(@NotNull OfflinePlayer player, String worldName) {
		return createPlayerAccount(player);
	}
}
