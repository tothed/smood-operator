package presentation;

import api.MoodScore;
import api.MoodType;
import api.TaMoodScoreService;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by S2QAH9 on 8. 2. 2017.
 */
@Named
@SessionScoped
public class ToneView implements Serializable{

    private String text;
    private long joy;

    public long getJoy() {
        return joy;
    }

    public void setJoy(long joy) {
        this.joy = joy;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setExcpetions(String excpetions) {
        this.excpetions = excpetions;
    }

    public String getExcpetions() {
        return excpetions;
    }

    private String excpetions;

    @PostConstruct
    private void init(){

    }
public void analyze (){
    TaMoodScoreService service =new TaMoodScoreService();
    try {
        List<MoodScore> result = service.analyze(text, EnumSet.of(MoodType.JOY));
       joy = Iterables.find(result, new Predicate<MoodScore>() {
           public boolean apply(MoodScore input) {
               return input.getMoodType() == MoodType.JOY;
           }
       }).getScore();
    } catch (Exception ex){
        ex.printStackTrace();
        excpetions = ex.getMessage();
    }
}
}
