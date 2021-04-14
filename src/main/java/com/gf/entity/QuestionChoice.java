package com.gf.entity;

/**
 * @author JusChui
 * @ClassName QuestionChoice.java
 * @Date 2021年04月12日 19:19:00
 * @Description
 */
public class QuestionChoice {

    private Long choiceId;
    private String questionId;
    private String choiceName;
    private String choice;

    public Long getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(Long choiceId) {
        this.choiceId = choiceId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getChoiceName() {
        return choiceName;
    }

    public void setChoiceName(String choiceName) {
        this.choiceName = choiceName;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }
}
