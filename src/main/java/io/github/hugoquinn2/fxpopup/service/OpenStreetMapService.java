package io.github.hugoquinn2.fxpopup.service;

import java.net.HttpURLConnection;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

public class OpenStreetMapService {
    public static String[] fetchSuggestions(String query) {
        String urlString = "https://nominatim.openstreetmap.org/search?q=" + query + "&format=json&addressdetails=1&limit=5";
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "JavaFX-OpenStreetMap-App");

            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder response = new StringBuilder();
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }
            scanner.close();

            JSONArray jsonArray = new JSONArray(response.toString());
            String[] suggestions = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String displayName = obj.getString("display_name");
                suggestions[i] = displayName;
            }
            return suggestions;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
