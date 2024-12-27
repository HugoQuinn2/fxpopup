package io.github.hugoquinn2.fxpopup.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.github.hugoquinn2.fxpopup.config.FxPopupConfig;
import org.json.JSONArray;
import org.json.JSONObject;

public class OpenStreetMapService {

    /*
    *   Search a list of address using Open Street Map
    *
    *   @param query, query search, example: "1600 Amphitheatre Parkway, Mountain View"
    *   @param limit, max size for recommendations
    *   @return recommendations, String list of recommendations reformated.
    * */

    public static List<String> searchAddress(String query, int limit) {
        List<String> recommendations = new ArrayList<>();
        try {
            String urlString = String.format("%s?q=%s&format=json&addressdetails=1&limit=%d", FxPopupConfig.NOMINATIM_URL,
                    query.replace(" ", "%20"), limit);
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Java OpenStreetMap Client");

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONArray jsonArray = new JSONArray(response.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    recommendations.add(jsonObject.optString("display_name"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recommendations;
    }

}
