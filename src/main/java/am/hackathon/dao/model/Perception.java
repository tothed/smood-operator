package am.hackathon.dao.model;

import com.google.common.base.Function;

import java.util.Date;

/**
 *
 */
public class Perception {

    public static final Function<Perception,Date> GET_DATE = new Function<Perception, Date>() {
        @Override
        public Date apply(Perception input) {
            return input.date;
        }
    };

    private String id;
    private Date date;
    private String username;
    private double score;
    private String reason;

    public Perception(String id) {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Perception{");
        sb.append("id='").append(id).append('\'');
        sb.append(", date=").append(date);
        sb.append(", username='").append(username).append('\'');
        sb.append(", score=").append(score);
        sb.append(", reason='").append(reason).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
