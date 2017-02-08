package presentation;

import api.TaMoodScoreService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

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
       joy =  service.analyze(text);
    } catch (Exception ex){
        excpetions = ex.getMessage();
    }
}
}
