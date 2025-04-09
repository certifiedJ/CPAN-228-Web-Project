package com.CPAN228.Project.controller;


import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// Admin Distribution Centre
public class DistributionCentreController {
    @GetMapping("/api/distribution-centres")
    public String getAllDistributionCentres(Model model) {
        return "admin-distribution-centres";
    }
}
