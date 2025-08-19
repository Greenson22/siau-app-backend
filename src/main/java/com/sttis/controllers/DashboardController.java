package com.sttis.controllers;

import com.sttis.dto.ActionItemDTO;
import com.sttis.dto.DashboardSummaryDTO;
import com.sttis.dto.PendaftaranChartDTO;
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

    @GetMapping("/pendaftaran-chart")
    public ResponseEntity<PendaftaranChartDTO> getPendaftaranChart() {
        PendaftaranChartDTO chartData = dashboardService.getPendaftaranChartData();
        return ResponseEntity.ok(chartData);
    }

    /**
     * ENDPOINT BARU untuk data "action items" di dashboard.
     * Method: GET
     * URL: /api/dashboard/action-items
     */
    @GetMapping("/action-items")
    public ResponseEntity<ActionItemDTO> getActionItems() {
        ActionItemDTO actionItems = dashboardService.getDashboardActionItems();
        return ResponseEntity.ok(actionItems);
    }
}