package api;

import java.util.List;

/***
 * Represents the mood score returned from Mood Score Service
 */
public class MoodScore {
    private long score;
    private MoodType moodType;
    private List<String> reasons;

    public MoodScore(long score, MoodType moodType, List<String> reasons) {
        this.score = score;
        this.moodType = moodType;
        this.reasons = reasons;
    }

    public long getScore() {
        return score;
    }
    public MoodType getMoodType() {
        return moodType;
    }
    public List<String> getReasons() {
        return reasons;
    }
}
