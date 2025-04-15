package com.CPAN228.Project.controller;

import com.CPAN228.Project.data.ClothesRepository;
//import com.CPAN228.Project.model.DistributionCentres;

//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
////import org.springframework.web.client.RestTemplate;

//import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private ClothesRepository clothesRepository;

//    @Autowired
//    private DistributionCentreService distributionCentreService;

    @GetMapping
    public String adminHome(Model model) {
        return "admin"; 
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/clothes/delete")
    public String deleteClothes(@RequestParam Long id) {
        clothesRepository.deleteById(id);
        return "redirect:/warehouse/clothes";
    }

}