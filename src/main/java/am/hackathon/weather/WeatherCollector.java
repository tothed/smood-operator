package am.hackathon.weather;

import com.cloudant.client.api.Database;
import com.cloudant.http.Http;
import com.cloudant.http.HttpConnection;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import wasdev.sample.servlet.CloudantClientMgr;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */

public class WeatherCollector {

    Location[] locations = new Location[]{
            new Location("Bratislava", "48.1357803/17.1159171"),
            new Location("London", "51.514427/-0.080308"),
            new Location("Zurich", "47.360085/8.535046"),
            new Location("New York", "40.758959/-73.973670"),
            new Location("Hong Kong", "22.280202/114.173618")};

//    @Schedule(dayOfMonth = "*/2", hour = "19", minute = "30")
    public void collect() {
        try {
            collect(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void collect(Function1<String> proxy) throws IOException {
        Database db=CloudantClientMgr.getDB(proxy);
        for (Location location : locations) {
            HttpConnection conn = Http.GET(new URL(getURL() + "/api/weather/v1/geocode/" + location.coord + "/forecast/hourly/48hour.json?units=m&language=en-US"));
            if (proxy != null)
                conn.connectionFactory.setProxy(new URL(proxy.exec()));
            String result = conn.execute().responseAsString();
            JsonObject obj = (JsonObject) new JsonParser().parse(result);
            JsonArray forecasts = obj.getAsJsonArray("forecasts");
            for (int i = 0; i < forecasts.size(); i++) {
                JsonObject forecast = forecasts.get(i).getAsJsonObject();
                Map<String, Object> map = new HashMap<>();
                map.put("date", forecast.get("fcst_valid_local"));
                map.put("type", "forecast");
                map.put("location", location.name);
                map.put("feels_like", forecast.get("feels_like"));
                map.put("cloudiness", forecast.get("phrase_32char"));
                db.save(map);
            }
        }
        db.ensureFullCommit();
    }

    /**
     * hardcoded for now lookup once in prod
     *
     * @return
     */
    private String getURL() {
        return "https://415de500-816c-4e48-9e91-a8335c975245:3zRuhqeRaT@twcservice.mybluemix.net";
    }

    private static class Location {
        String name;
        String coord;

        public Location(String name, String coord) {
            this.name = name;
            this.coord = coord;
        }
    }
}
