package com.CPAN228.Project.controller;

import com.CPAN228.Project.model.DistributionCentres;
import com.CPAN228.Project.model.DistributionItemDTO;
import com.CPAN228.Project.model.ItemsDTO;
import com.CPAN228.Project.service.DistributionCentreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/distribution-centres")
@SessionAttributes("availableItems")
public class DistributionCentreController {

    private final DistributionCentreService distributionCentreService;

    public DistributionCentreController(DistributionCentreService distributionCentreService) {
        this.distributionCentreService = distributionCentreService;
    }

    @GetMapping("/all")
    public String getAllDistributionCentres(Model model) {
        try {
            List<DistributionCentres> centres = distributionCentreService.getAllCentres();
            model.addAttribute("centres", centres);
        } catch (Exception e) {
            model.addAttribute("error", "Failed to retrieve distribution centres: " + e.getMessage());
        }
        return "admin-distribution-centres";
    }

    @GetMapping("/find")
    public String findDistributionCentres(Model model) {
        try {
            List<ItemsDTO> items = distributionCentreService.getAllItems();
            // Extract unique brands
            List<String> brands = items.stream()
                    .map(ItemsDTO::getBrand)
                    .distinct()
                    .collect(Collectors.toList());

            // Add brands to the model
            model.addAttribute("brands", brands);
        } catch (Exception e) {
            model.addAttribute("error", "Failed to retrieve distribution centres: " + e.getMessage());
        }
        return "requestForm";
    }

    @PostMapping("/request")
    public String requestItem(
            @RequestParam String name,
            @RequestParam String brand,
            Model model) {

        try {
            List<DistributionItemDTO> availableItems = distributionCentreService.findItemsByNameAndBrand(name, brand);
            model.addAttribute("availableItems", availableItems);
            model.addAttribute("searchName", name);
            model.addAttribute("searchBrand", brand);
            return "itemConfirmation";
        } catch (Exception e) {
            model.addAttribute("error", "Error processing request: " + e.getMessage());
            return "requestError";
        }
    }

    @PostMapping("/confirm")
    public String confirmItemRequest(
            @RequestParam("selectedItemIndex") int selectedItemIndex,
            Model model) {

        try {
            // Get the previously stored list of items from the model
            @SuppressWarnings("unchecked")
            List<DistributionItemDTO> availableItems =
                    (List<DistributionItemDTO>) model.asMap().get("availableItems");

            // If items aren't in the model, we might need to fetch them again
            if (availableItems == null || availableItems.isEmpty()) {
                model.addAttribute("error", "Session expired or no items available");
                return "requestError";
            }

            // Validate index
            if (selectedItemIndex < 0 || selectedItemIndex >= availableItems.size()) {
                model.addAttribute("error", "Invalid item selection");
                return "requestError";
            }

            // Get selected item and process
            DistributionItemDTO selectedItem = availableItems.get(selectedItemIndex);
            boolean success = distributionCentreService.confirmItemRequest(selectedItem);

            if (success) {
                model.addAttribute("message", "Stock replenished successfully!");
                return "requestSuccess";
            } else {
                model.addAttribute("error", "Failed to update inventory");
                return "requestError";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error processing confirmation: " + e.getMessage());
            return "requestError";
        }
    }
}