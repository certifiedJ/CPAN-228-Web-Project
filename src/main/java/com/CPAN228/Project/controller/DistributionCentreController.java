package com.CPAN228.Project.controller;


import com.CPAN228.Project.model.DistributionCentres;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
@RequestMapping("/distribution-centres")
public class DistributionCentreController {

    @GetMapping("/all")
    public String getAllDistributionCentres(Model model) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8081/api/distribution-centres";
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("admin", "admin");
        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                ObjectMapper objectMapper = new ObjectMapper();
                List<DistributionCentres> centres = objectMapper.readValue(
                        response.getBody(),
                        new TypeReference<List<DistributionCentres>>() {}
                );
                model.addAttribute("centres", centres);
            } else {
                model.addAttribute("error", "Failed to retrieve data: " + response.getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();  // Detailed error logging
            model.addAttribute("error", "Failed to parse distribution centres data: " + e.getMessage());
        }
        return "admin-distribution-centres";
    }

    @GetMapping("/find")
    public String findDistributionCentres(Model model) {
        return "requestForm";
    }
}
