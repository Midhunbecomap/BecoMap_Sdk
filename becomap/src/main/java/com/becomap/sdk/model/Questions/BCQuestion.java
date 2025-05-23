package com.becomap.sdk.model.Questions;

import java.util.List;

public class BCQuestion {
    public enum BCQuestionType {
        MCQ_SINGLE_ANSWER,
        TEXT,
        NUMBER,
        BOOLEAN
    }

    private String id;
    private String question;
    private BCQuestionType type;
    private List<String> options;

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

    public BCQuestionType getType() {
        return type;
    }

    public void setType(BCQuestionType type) {
        this.type = type;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
// Getters and setters (or public fields if you prefer)
}
