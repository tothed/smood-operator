package presentation;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by S2QAH9 on 8. 2. 2017.
 */
@Named
@SessionScoped
public class IndexView implements Serializable{

    @PostConstruct
    private void init(){
        question = "Hi Frank! How is your mood today?";
        step=0;
        selectedDomains= new ArrayList<>();
    }

    private String question;
    private int step;
    private List<String> selectedDomains;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void save(int step){
        this.step=step+1;
        if (this.step==1){
            question ="Good to hear that. Why are you feeling so good?";
        } else {
            question ="...not surprisingly..I know that your mates feels the same way.";
        }
    }

    public void reset(){
        init();
    }

    public int getStep() {
        return step;
    }

    public List<String> getSelectedDomains() {
        return selectedDomains;
    }

    public void setSelectedDomains(List<String> selectedDomains) {
        this.selectedDomains = selectedDomains;
    }
}
