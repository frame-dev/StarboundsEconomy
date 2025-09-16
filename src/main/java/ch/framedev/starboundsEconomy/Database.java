package ch.framedev.starboundsEconomy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.UUID;
import java.util.logging.Level;

public class Database {

    private final String TABLE_NAME = "economy";
    private final String COLUMN_UUID = "uuid";
    private final String COLUMN_BALANCE = "stars";

    public Database() {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COLUMN_UUID + " VARCHAR(36) PRIMARY KEY," +
                COLUMN_BALANCE + " DOUBLE DEFAULT 0.0" +
                ")";
        try (Connection conn = PostgreSQL.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (java.sql.SQLException e) {
            StarboundsEconomy.getInstance().getLogger().log(Level.SEVERE, "Could not create table " + TABLE_NAME, e);
        }
    }

    public boolean insertPlayer(UUID uuid) {
        String sql = "INSERT INTO " + TABLE_NAME + " (" + COLUMN_UUID + ", " + COLUMN_BALANCE + ") VALUES (?, ?)";
        try (Connection conn = PostgreSQL.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());
            pstmt.setInt(2, 0);
            pstmt.executeUpdate();
            return true;
        } catch (java.sql.SQLException e) {
            StarboundsEconomy.getInstance().getLogger().log(Level.SEVERE, "Could not insert player " + uuid, e);
            return false;
        }
    }

    public boolean playerExists(UUID uuid) {
        String sql = "SELECT 1 FROM " + TABLE_NAME + " WHERE " + COLUMN_UUID + " = ?";
        try (Connection conn = PostgreSQL.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());
            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (java.sql.SQLException e) {
            StarboundsEconomy.getInstance().getLogger().log(Level.SEVERE, "Could not check if player exists " + uuid, e);
            return false;
        }
    }

    public boolean setBalance(UUID uuid, double amount) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + COLUMN_BALANCE + " = ? WHERE " + COLUMN_UUID + " = ?";
        try (Connection conn = PostgreSQL.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, amount);
            pstmt.setString(2, uuid.toString());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (java.sql.SQLException e) {
            StarboundsEconomy.getInstance().getLogger().log(Level.SEVERE, "Could not set balance for player " + uuid, e);
            return false;
        }
    }

    public double getBalance(UUID uuid) {
        String sql = "SELECT " + COLUMN_BALANCE + " FROM " + TABLE_NAME + " WHERE " + COLUMN_UUID + " = ?";
        try (Connection conn = PostgreSQL.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());
            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(COLUMN_BALANCE);
                } else {
                    return 0.0;
                }
            }
        } catch (java.sql.SQLException e) {
            StarboundsEconomy.getInstance().getLogger().log(Level.SEVERE, "Could not get balance for player " + uuid, e);
            return 0.0;
        }
    }
}
