package am.hackathon.dao.model;

import java.util.Date;

/**
 *
 */
public class Event {
    private String id;
    private Date date;
    private String nameEvent;
    private String location;
    private String globalFunction;

    public Event(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNameEvent() {
        return nameEvent;
    }

    public void setNameEvent(String nameEvent) {
        this.nameEvent = nameEvent;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGlobalFunction() {
        return globalFunction;
    }

    public void setGlobalFunction(String globalFunction) {
        this.globalFunction = globalFunction;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Event{");
        sb.append("id='").append(id).append('\'');
        sb.append(", date=").append(date);
        sb.append(", nameEvent='").append(nameEvent).append('\'');
        sb.append(", location='").append(location).append('\'');
        sb.append(", globalFunction='").append(globalFunction).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
