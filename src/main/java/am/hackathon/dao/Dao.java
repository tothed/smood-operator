package am.hackathon.dao;

import am.hackathon.dao.model.Event;
import am.hackathon.dao.model.Forecast;
import am.hackathon.dao.model.Perception;
import am.hackathon.weather.Function1;
import com.cloudant.client.api.Database;
import com.cloudant.client.api.views.Key;
import com.cloudant.client.api.views.UnpaginatedRequestBuilder;
import com.cloudant.client.api.views.ViewResponse;
import wasdev.sample.servlet.CloudantClientMgr;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class Dao {
    private final Database db;
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
                                                                    //2017-02-07T16:00:00+0100
    private static final SimpleDateFormat forecastFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    public Dao() {
        this.db = CloudantClientMgr.getDB(null);
    }

    public Dao(Function1<String> proxy) {
        this.db = CloudantClientMgr.getDB(proxy);
    }

    public List<Event> listEvents() {
        UnpaginatedRequestBuilder<String, Map> res = db.getViewRequestBuilder("events", "event").newRequest(Key.Type.STRING, Map.class);
        try {
            ViewResponse<String, Map> response = res.includeDocs(true).build().getResponse();
            List<Event> events = new ArrayList<>();
            for (Map<String, String> event : response.getValues()) {
                events.add(toEvent(event));
            }
            return events;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Forecast> listForecasts() {
        UnpaginatedRequestBuilder<String, Map> res = db.getViewRequestBuilder("forecasts", "forecast").newRequest(Key.Type.STRING, Map.class);
        try {
            ViewResponse<String, Map> response = res.includeDocs(true).build().getResponse();
            List<Forecast> forecasts = new ArrayList<>();
            for (Map<String, Object> map : response.getValues()) {
                forecasts.add(toForecast(map));
            }
            return forecasts;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public List<Forecast> listForecasts(String location) {
        UnpaginatedRequestBuilder<String, Map> res = db.getViewRequestBuilder("forecasts", "forecast").newRequest(Key.Type.STRING, Map.class);
        try {
            ViewResponse<String, Map> response = res.includeDocs(true).build().getResponse();
            List<Forecast> forecasts = new ArrayList<>();
            for (Map<String, Object> map : response.getValues()) {
                final Forecast forecast = toForecast(map);
                if (forecast.getLocation().equals(location))
                    forecasts.add(forecast);
            }
            return forecasts;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Perception> listPerceptions(String user) {
        UnpaginatedRequestBuilder<String, Map> res = db.getViewRequestBuilder("perceptions", "perception").newRequest(Key.Type.STRING, Map.class);
        try {
            ViewResponse<String, Map> response = res.includeDocs(true).build().getResponse();
            List<Perception> forecasts = new ArrayList<>();
            for (Map<String, Object> map : response.getValues()) {
                    final Perception perception = toPerception(map);
                    if (perception.getUsername().equals(user))
                        forecasts.add(perception);
            }
            return forecasts;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Perception toPerception(Map<String, Object> map) throws ParseException {
        Perception perception = new Perception((String) map.get("_id"));
        perception.setReason((String) map.get("Reason"));
        perception.setDate(formatter.parse((String) map.get("Date")));
        perception.setScore(Double.parseDouble((String) map.get("Score")));
        perception.setUsername((String) map.get("User_Name"));
        return perception;
    }

    private Forecast toForecast(Map<String, Object> map) throws ParseException {
        Forecast forecast = new Forecast((String) map.get("_id"));
        forecast.setLocation((String)map.get("location"));
        forecast.setCloudiness((String)map.get("cloudiness"));
        forecast.setFeels_like((Double) map.get("feels_like"));
        forecast.setDate(forecastFormatter.parse((String)map.get("date")));
        return forecast;
    }

    private Event toEvent(Map<String, String> map) throws ParseException {
        Event event = new Event(map.get("_id"));
        event.setNameEvent(map.get("Event"));
        event.setDate(formatter.parse(map.get("Date")));
        event.setGlobalFunction(map.get("Global Function"));
        event.setLocation(map.get("Location"));
        return event;
    }
}
