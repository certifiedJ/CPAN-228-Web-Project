package com.CPAN228.Project.controller;

import com.CPAN228.Project.data.ClothesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private ClothesRepository clothesRepository;
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
