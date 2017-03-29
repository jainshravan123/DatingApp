package myapp.dating.shravan.datingapp1.bean;

import java.util.ArrayList;

/**
 * Created by admin on 24/07/16.
 */
public class QuestionAsked
{

    private String id;
    private String question;
    private ArrayList<String> answer;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<String> getAnswer() {
        return answer;
    }

    public void setAnswer(ArrayList<String> answer) {
        this.answer = answer;
    }
}
