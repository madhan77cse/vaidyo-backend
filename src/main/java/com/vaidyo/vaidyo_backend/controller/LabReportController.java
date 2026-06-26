package com.vaidyo.vaidyo_backend.controller;

import com.vaidyo.vaidyo_backend.dto.LabReportResponse;
import com.vaidyo.vaidyo_backend.service.LabReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/patient")
@CrossOrigin(origins = "*")
public class LabReportController {

    private final LabReportService labReportService;

    public LabReportController(LabReportService labReportService) {
        this.labReportService = labReportService;
    }

    @PostMapping("/lab-report")
    public ResponseEntity<?> analyzeLabReport(
            @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("Please upload a file.");
            }

            String summary = labReportService.analyzeReport(file);
            LabReportResponse response = new LabReportResponse(
                    summary, labReportService.getDisclaimer());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Error analyzing report: " + e.getMessage());
        }
    }
}