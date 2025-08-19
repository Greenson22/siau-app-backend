// program/java-spring-boot/com/sttis/controllers/DashboardController.java
package com.sttis.controllers;

import com.sttis.dto.DashboardSummaryDTO;
import com.sttis.dto.PendaftaranChartDTO; // <-- IMPORT BARU
import com.sttis.services.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryDTO> getSummary() {
        DashboardSummaryDTO summary = dashboardService.getDashboardSummary();
        return ResponseEntity.ok(summary);
    }

    /**
     * ENDPOINT BARU untuk data chart pendaftaran.
     * Method: GET
     * URL: /api/dashboard/pendaftaran-chart
     */
    @GetMapping("/pendaftaran-chart")
    public ResponseEntity<PendaftaranChartDTO> getPendaftaranChart() {
        PendaftaranChartDTO chartData = dashboardService.getPendaftaranChartData();
        return ResponseEntity.ok(chartData);
    }
}