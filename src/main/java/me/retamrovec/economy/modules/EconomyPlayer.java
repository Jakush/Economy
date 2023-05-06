package me.retamrovec.economy.modules;

public class EconomyPlayer {

	private Double balance;
	private final String playerName;

	public EconomyPlayer(String playerName, Double balance) {
		this.balance = balance;
		this.playerName = playerName;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public String getPlayerName() {
		return playerName;
	}
}
