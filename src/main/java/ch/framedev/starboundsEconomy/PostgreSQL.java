package ch.framedev.starboundsEconomy;

import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreSQL {

    public static Connection getConnection() throws SQLException {
        FileConfiguration config = StarboundsEconomy.getInstance().getConfig();
        String host = config.getString("database.host", "localhost");
        int port = config.getInt("database.port", 5432);
        String dbName = config.getString("database.name", "your_database");
        String user = config.getString("database.user", "your_username");
        String password = config.getString("database.password", "your_password");
        String URL = "jdbc:postgresql://" + host + ":" + port + "/" + dbName;

        return DriverManager.getConnection(URL, user, password);
    }
}
