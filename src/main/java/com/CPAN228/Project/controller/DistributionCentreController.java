package com.CPAN228.Project.controller;

import com.CPAN228.Project.data.ClothesRepository;
import com.CPAN228.Project.model.*;
import com.CPAN228.Project.service.DistributionCentreService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/distribution-centres")
@SessionAttributes("availableItems")
public class DistributionCentreController {

    private final DistributionCentreService distributionCentreService;
    private final ClothesRepository clothesRepository;

    public DistributionCentreController(DistributionCentreService distributionCentreService, ClothesRepository clothesRepository) {
        this.distributionCentreService = distributionCentreService;
        this.clothesRepository = clothesRepository;
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
    @SuppressWarnings("unchecked")
    public String confirmItemRequest(HttpSession session,
                                     @RequestParam("selectedItemIndex") int selectedItemIndex,
                                     @RequestParam("replenishNumber") int replenishNumber) {
        // Retrieve the list of available items from the session
        List<DistributionItemDTO> availableItems = (List<DistributionItemDTO>) session.getAttribute("availableItems");

        // Check if the session data is valid and the index is within bounds
        if (availableItems == null || selectedItemIndex < 0 || selectedItemIndex >= availableItems.size()) {
            return "error"; // Redirect to an error page
        }

        // Get the selected item using the index
        DistributionItemDTO selectedItem = availableItems.get(selectedItemIndex);

        // Extract the item details
        String brand = selectedItem.getBrand();
        String name = selectedItem.getName();
        String distributionCentreName = selectedItem.getDistributionCentreName();
        int availableQuantity = selectedItem.getQuantity();

        // Basic validation: ensure replenishNumber is positive and does not exceed available quantity
        if (replenishNumber <= 0 || replenishNumber > availableQuantity) {
            return "error"; // Redirect to an error page
        }

        // Step 2: Check if cloth exists in warehouse
        Optional<Clothes> clothOpt = clothesRepository.findByBrandAndName(brand, name);

        // Step 3: If exists, update quantity
        if (clothOpt.isPresent()) {
            Clothes cloth = clothOpt.get();
            cloth.setQuantity(cloth.getQuantity() + replenishNumber);
            clothesRepository.save(cloth);
        }
        // Step 4: If not exists, insert new cloth
        else {
            Clothes newCloth = new Clothes();
            newCloth.setBrand(brand);
            newCloth.setYear(2025); //default year
            newCloth.setPrice(199.22);
            newCloth.setName(name);
            newCloth.setQuantity(replenishNumber);
            clothesRepository.save(newCloth);
        }

        // reduce item quantity on distribution centre side
        // Get centreId
        List<DistributionCentres> centres = distributionCentreService.getAllCentres();
        Long centreId = null;
        for (DistributionCentres centre : centres) {
            if (centre.getName().equals(distributionCentreName)) {
                centreId = centre.getId();
                break;
            }
        }
        if (centreId == null) {
            return "error"; // Centre not found
        }

        // Get itemId
        List<ItemsDTO> items = distributionCentreService.getAllItems();
        Long itemId = null;
        for (ItemsDTO item : items) {
            if (item.getBrand().equals(brand) && item.getName().equals(name)) {
                itemId = item.getId();
                break;
            }
        }
        if (itemId == null) {
            return "error"; // Item not found
        }

        // Call service to update quantity
        boolean success = distributionCentreService.updateItemQuantity(
                centreId.intValue(), itemId.intValue(), replenishNumber
        );
        if (!success) {
            return "error"; // API call failed
        }

        // Clean up the session
        session.removeAttribute("availableItems");

        // Redirect to a placeholder page (to be updated in later steps)
        return "redirect:/warehouse/clothes";

    }
}