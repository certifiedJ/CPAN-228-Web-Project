package com.CPAN228.Project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.CPAN228.Project.model.Clothes;
import com.CPAN228.Project.data.ClothesRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/warehouse")
@SessionAttributes("clothesPool")
public class WarehouseController {

    @Autowired
    private ClothesRepository clothesRepository;

    @ModelAttribute(name = "clothes")
    public Clothes clothes() {
        return new Clothes();
    }

    @GetMapping
    public String showDesignForm() {
        return "design";
    }

    @PostMapping
    public String processClothesAddition(@ModelAttribute Clothes clothes) {
        clothesRepository.save(clothes);
        return "redirect:/warehouse/clothes";
    }

    @GetMapping("/clothes")
    public String getClothesPaginated(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "5") int size,
                                      Model model) {
        Page<Clothes> clothesPage = clothesRepository.findAll(PageRequest.of(page, size));
        model.addAttribute("clothesList", clothesPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", clothesPage.getTotalPages());
        model.addAttribute("totalItems", clothesPage.getTotalElements());
        return "clothesList";
    }

    @GetMapping("/clothes/sort")
    public String showClothesSortedByBrand(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {
        Page<Clothes> clothesPage = clothesRepository.findAllByOrderByBrandAsc(PageRequest.of(page, size));
        model.addAttribute("clothesList", clothesPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", clothesPage.getTotalPages());
        model.addAttribute("totalItems", clothesPage.getTotalElements());
        model.addAttribute("sortedByBrand", true);
        return "clothesList";
    }

    @GetMapping("/clothes/byBrand")
    public String showClothesByBrand(@RequestParam String brand, Model model) {
        List<Clothes> clothesList = clothesRepository.findByBrand(brand);
        model.addAttribute("clothesList", clothesList);
        model.addAttribute("filterApplied", true);
        model.addAttribute("filterDescription", "Brand: " + brand);
        return "clothesList";
    }

}