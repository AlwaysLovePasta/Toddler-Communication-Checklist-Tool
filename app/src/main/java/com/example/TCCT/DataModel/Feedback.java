package com.example.TCCT.DataModel;

public class Feedback {

    String feedbackContent, feedbackRating;

    public Feedback(String feedbackContent, String feedbackRating) {
        this.feedbackContent = feedbackContent;
        this.feedbackRating = feedbackRating;
    }

    public String getFeedbackContent() {
        return feedbackContent;
    }

    public void setFeedbackContent(String feedbackContent) {
        this.feedbackContent = feedbackContent;
    }

    public String getFeedbackRating() {
        return feedbackRating;
    }

    public void setFeedbackRating(String feedbackRating) {
        this.feedbackRating = feedbackRating;
    }
}
