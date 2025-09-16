package ch.framedev.starboundsEconomy.utils;

import java.util.UUID;

public interface DatabaseHelper {

    boolean setBalance(UUID uuid, int balance);
    int getBalance(UUID uuid);
    boolean playerExists(UUID uuid);
    boolean insertPlayer(UUID uuid);
}
