package ch.framedev.starboundsEconomy.utils;

import ch.framedev.starboundsEconomy.StarboundsEconomy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;

public class Database implements DatabaseHelper {

    private final String TABLE_NAME = "economy";
    private final String COLUMN_UUID = "uuid";
    private final String COLUMN_BALANCE = "stars";

    public Database() {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COLUMN_UUID + " VARCHAR(36) PRIMARY KEY," +
                COLUMN_BALANCE + " INTEGER DEFAULT 0" +
                ")";
        try (Connection conn = PostgreSQL.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            StarboundsEconomy.getInstance().getLogger().log(Level.SEVERE, "Could not create table " + TABLE_NAME, e);
        }
    }

    @Override
    public boolean insertPlayer(UUID uuid) {
        String sql = "INSERT INTO " + TABLE_NAME + " (" + COLUMN_UUID + ", " + COLUMN_BALANCE + ") VALUES (?, ?)";
        try (Connection conn = PostgreSQL.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());
            pstmt.setInt(2, 0);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            StarboundsEconomy.getInstance().getLogger().log(Level.SEVERE, "Could not insert player " + uuid, e);
            return false;
        }
    }

    public boolean playerExists(UUID uuid) {
        String sql = "SELECT 1 FROM " + TABLE_NAME + " WHERE " + COLUMN_UUID + " = ?";
        try (Connection conn = PostgreSQL.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            StarboundsEconomy.getInstance().getLogger().log(Level.SEVERE, "Could not check if player exists " + uuid, e);
            return false;
        }
    }

    public boolean setBalance(UUID uuid, int amount) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + COLUMN_BALANCE + " = ? WHERE " + COLUMN_UUID + " = ?";
        try (Connection conn = PostgreSQL.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, amount);
            pstmt.setString(2, uuid.toString());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            StarboundsEconomy.getInstance().getLogger().log(Level.SEVERE, "Could not set balance for player " + uuid, e);
            return false;
        }
    }

    public int getBalance(UUID uuid) {
        String sql = "SELECT " + COLUMN_BALANCE + " FROM " + TABLE_NAME + " WHERE " + COLUMN_UUID + " = ?";
        try (Connection conn = PostgreSQL.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(COLUMN_BALANCE);
                } else {
                    return 0;
                }
            }
        } catch (SQLException e) {
            StarboundsEconomy.getInstance().getLogger().log(Level.SEVERE, "Could not get balance for player " + uuid, e);
            return 0;
        }
    }
}
