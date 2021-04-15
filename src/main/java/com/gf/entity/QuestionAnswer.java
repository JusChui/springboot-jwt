package com.gf.entity;

/**
 * @author JusChui
 * @ClassName QuestionAnswer.java
 * @Date 2021年04月12日 19:21:00
 * @Description
 */
public class QuestionAnswer {

    private Long answerId;
    private String questionId;
    private String rightChoice;

    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getRightChoice() {
        return rightChoice;
    }

    public void setRightChoice(String rightChoice) {
        this.rightChoice = rightChoice;
    }
}
