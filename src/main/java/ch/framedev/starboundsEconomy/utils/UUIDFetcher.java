package ch.framedev.starboundsEconomy.utils;

import ch.framedev.starboundsEconomy.StarboundsEconomy;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.logging.Level;

public class UUIDFetcher {

    public static UUID getUUID(String name) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    String json = response.toString();
                    int idIndex = json.indexOf("\"id\":\"") + 6;
                    int idEnd = json.indexOf("\"", idIndex);
                    String id = json.substring(idIndex, idEnd);
                    return fromString(id);
                }
            }
        } catch (Exception e) {
            StarboundsEconomy.getInstance().getLogger().log(Level.SEVERE, "Could not fetch UUID for player " + name, e);
        }
        return null;
    }

    private static UUID fromString(String id) {
        return UUID.fromString(id.replaceFirst(
                "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",
                "$1-$2-$3-$4-$5"
        ));
    }
}
