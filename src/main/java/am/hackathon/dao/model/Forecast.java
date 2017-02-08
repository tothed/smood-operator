package am.hackathon.dao.model;

import java.util.Date;

/**
 *
 */
public class Forecast {
    private String id;
    private Date date;
    private double feels_like;
    private String location;
    private String cloudiness;

    public Forecast(String id) {
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

    public double getFeels_like() {
        return feels_like;
    }

    public void setFeels_like(double feels_like) {
        this.feels_like = feels_like;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCloudiness() {
        return cloudiness;
    }

    public void setCloudiness(String cloudiness) {
        this.cloudiness = cloudiness;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Forecast{");
        sb.append("id='").append(id).append('\'');
        sb.append(", date=").append(date);
        sb.append(", feels_like=").append(feels_like);
        sb.append(", location='").append(location).append('\'');
        sb.append(", cloudiness='").append(cloudiness).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
