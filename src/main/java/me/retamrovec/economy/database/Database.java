package me.retamrovec.economy.database;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {

	private final String DATABASE;
	private final String HOST;
	private final String PASSWORD;
	private final String USER;
	private final Integer PORT;
	private Connection connection;

	public Database(@NotNull FileConfiguration config) {
		DATABASE = config.getString("database.name");
		HOST = config.getString("database.host");
		PASSWORD = config.getString("database.password");
		USER = config.getString("database.username");
		PORT = config.getInt("database.port");
	}

	public boolean isConnected() {
		return connection != null;
	}

	public Connection getConnection() {
		return connection;
	}

	public void connect() {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + "?useSSL=false", USER, PASSWORD);
		} catch (SQLException e) {
			Bukkit.getLogger().severe("[Economy] Couldn't create connection to database!");
			e.printStackTrace();
		}
		if (isConnected()) {
			try (PreparedStatement ps = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Econ (Player text, Balance double);")) {
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void disconnect() {
		if (isConnected()) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public PreparedStatement prepareStatement(String sql) throws SQLException {
		if (!isConnected()) connect();
		return connection.prepareStatement(sql);
	}
}
