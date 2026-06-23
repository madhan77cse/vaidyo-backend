package com.vaidyo.vaidyo_backend.dto;

public class SymptomCheckerResponse {

    private String analysis;
    private String disclaimer;

    public SymptomCheckerResponse(String analysis, String disclaimer) {
        this.analysis = analysis;
        this.disclaimer = disclaimer;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }
}