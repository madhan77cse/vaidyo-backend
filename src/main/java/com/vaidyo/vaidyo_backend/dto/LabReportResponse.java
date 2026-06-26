package com.vaidyo.vaidyo_backend.dto;

public class LabReportResponse {

    private String summary;
    private String disclaimer;

    public LabReportResponse(String summary, String disclaimer) {
        this.summary = summary;
        this.disclaimer = disclaimer;
    }

    public String getSummary() { return summary; }
    public String getDisclaimer() { return disclaimer; }

    public void setSummary(String summary) { this.summary = summary; }
    public void setDisclaimer(String disclaimer) { this.disclaimer = disclaimer; }
}