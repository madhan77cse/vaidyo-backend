package com.vaidyo.vaidyo_backend.dto;

public class FoodRecommendationResponse {

    private String recommendation;
    private String disclaimer;

    public FoodRecommendationResponse(String recommendation, String disclaimer) {
        this.recommendation = recommendation;
        this.disclaimer = disclaimer;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }
}