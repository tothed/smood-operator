package am.hackathon.dao;

import am.hackathon.dao.model.Event;
import am.hackathon.dao.model.Forecast;
import am.hackathon.dao.model.Perception;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import java.util.Date;
import java.util.Map;

/**
 *
 */
public class AsSeries {


    public static Map<Date, Event> eventMap(Dao dao) {
        return Maps.uniqueIndex(dao.listEvents(), Event.GET_DATE);
    }

    public static Map<Date, Forecast> forecastMap(Dao dao, String location) {
        return Maps.uniqueIndex(dao.listForecasts(location), Forecast.GET_DATE);
    }

    public static Multimap<Date, Perception> perceptionMap(Dao dao) {
        return Multimaps.index(dao.listPerceptions(), Perception.GET_DATE);
    }
}
