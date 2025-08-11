package org.ironlegion.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HypixelApiUtil {


    private static final String HYPIXEL_API_KEY = "6f9239c2-d837-4196-95b8-8156f93de78b";
    private static final String BASE_URL = "https://api.hypixel.net/v2/";
    private static final String MOJANG_BASE_URL = "https://api.mojang.com/users/profiles/minecraft/";

    /**
     * Overloaded method to get player stats by username. It first gets the UUID, then fetches stats.
     *
     * @param username The Minecraft username.
     * @return A JsonObject containing the player's data, or null if the request fails.
     */
    public static JsonObject getPlayerStats(String username) {
        String uuid = getUUIDFromUsername(username);
        if (uuid != null) {
            return getPlayerStatsbyUUID(uuid);
        }
        return null;
    }

    /**
     * Calls the Hypixel API to get a player's stats and returns the JSON response.
     * This method is a foundational piece for building more complex bot commands.
     *
     * @param uuid The UUID of the player.
     * @return A JsonObject containing the player's data, or null if the request fails.
     */
    public static JsonObject getPlayerStatsbyUUID(String uuid) {
        // We'll use the Gson library for JSON parsing.
        Gson gson = new Gson();
        JsonObject playerStats = null;

        try {
            URL url = new URL(BASE_URL + "skyblock/profiles?uuid=" + uuid);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set up the request method and headers.
            connection.setRequestMethod("GET");
            //connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setRequestProperty("API-Key", HYPIXEL_API_KEY);
            connection.setConnectTimeout(5000); // 5 seconds
            connection.setReadTimeout(5000);    // 5 seconds

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response from the input stream.
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse the response string into a JsonObject.
                playerStats = gson.fromJson(response.toString(), JsonObject.class);

                // Check for successful API response
                if (playerStats != null && playerStats.has("success") && playerStats.get("success").getAsBoolean()) {
                    System.out.println("Successfully fetched data for player with UUID: " + uuid);
                } else {
                    System.err.println("API call was not successful for UUID: " + uuid);
                    // You might want to log the error message from the API.
                    if (playerStats != null && playerStats.has("cause")) {
                        System.err.println("Cause: " + playerStats.get("cause").getAsString());
                    }
                    playerStats = null;
                }
            } else {
                // Handle non-200 responses, e.g., 403 Forbidden, 429 Too Many Requests.
                System.err.println("HTTP request failed with code: " + responseCode);
                try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String errorLine;
                    while ((errorLine = errorReader.readLine()) != null) {
                        errorResponse.append(errorLine);
                    }
                    JsonObject errorJson = gson.fromJson(errorResponse.toString(), JsonObject.class);
                    if (errorJson != null && errorJson.has("cause")) {
                        System.err.println("Specific API error cause: " + errorJson.get("cause").getAsString());
                    }
                } catch (Exception e) {
                    System.err.println("Could not read error stream for details.");
                }
            }
        } catch (Exception e) {
            // Catch any exceptions that occur during the process.
            e.printStackTrace();
        }

        return playerStats;
    }


    /**
     * Calls the Hypixel API to get a player's stats and returns the JSON response.
     * This method is a foundational piece for building more complex bot commands.
     *
     * @param ignUUID The UUID of the player.
     * @param profileUUID The UUID of the profile
     * @return A JsonObject containing the player's data, or null if the request fails.
     */
    public static JsonObject getPlayerProfileWithUUID(String ignUUID, String profileUUID) {
        // We'll use the Gson library for JSON parsing.
        Gson gson = new Gson();
        JsonObject playerStats = null;

        try {
            URL url = new URL(BASE_URL + "skyblock/profile?profile=" + profileUUID);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set up the request method and headers.
            connection.setRequestMethod("GET");
            //connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setRequestProperty("API-Key", HYPIXEL_API_KEY);


            connection.setConnectTimeout(5000); // 5 seconds
            connection.setReadTimeout(5000);    // 5 seconds

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response from the input stream.
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse the response string into a JsonObject.
                playerStats = gson.fromJson(response.toString(), JsonObject.class);

                // Check for successful API response
                if (playerStats != null && playerStats.has("success") && playerStats.get("success").getAsBoolean()) {
                    System.out.println("Successfully fetched data for player with UUID: " + profileUUID);
                } else {
                    System.err.println("API call was not successful for UUID: " + profileUUID);
                    // You might want to log the error message from the API.
                    if (playerStats != null && playerStats.has("cause")) {
                        System.err.println("Cause: " + playerStats.get("cause").getAsString());
                    }
                    playerStats = null;
                }
            } else {
                // Handle non-200 responses, e.g., 403 Forbidden, 429 Too Many Requests.
                System.err.println("HTTP request failed with code: " + responseCode);
                try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String errorLine;
                    while ((errorLine = errorReader.readLine()) != null) {
                        errorResponse.append(errorLine);
                    }
                    JsonObject errorJson = gson.fromJson(errorResponse.toString(), JsonObject.class);
                    if (errorJson != null && errorJson.has("cause")) {
                        System.err.println("Specific API error cause: " + errorJson.get("cause").getAsString());
                    }
                } catch (Exception e) {
                    System.err.println("Could not read error stream for details.");
                }
            }
        } catch (Exception e) {
            // Catch any exceptions that occur during the process.
            e.printStackTrace();
        }

        return playerStats;
    }

    /**
     * Calls the Mojang API to get a player's UUID from their username.
     *
     * @param username The Minecraft username.
     * @return The player's UUID as a String, or null if not found.
     */
    public static String getUUIDFromUsername(String username) {
        Gson gson = new Gson();
        String uuid = null;

        try {
            URL url = new URL(MOJANG_BASE_URL + username);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JsonObject mojangResponse = gson.fromJson(response.toString(), JsonObject.class);
                if (mojangResponse != null && mojangResponse.has("id")) {
                    // The Mojang API returns the UUID without hyphens, so we use it directly.
                    uuid = mojangResponse.get("id").getAsString();
                    System.out.println("Successfully resolved username " + username + " to UUID " + uuid);
                }
            } else {
                System.err.println("Mojang API request failed with code: " + responseCode);
                System.err.println("Player with username '" + username + "' not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return uuid;
    }

}
