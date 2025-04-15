package com.CPAN228.Project.service;

import com.CPAN228.Project.data.ClothesRepository;
import com.CPAN228.Project.model.Clothes;
import com.CPAN228.Project.model.DistributionCentres;
import com.CPAN228.Project.model.DistributionItemDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DistributionCentreServiceImpl implements DistributionCentreService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final ClothesRepository clothesRepository;

    @Autowired
    public DistributionCentreServiceImpl(ClothesRepository clothesRepository) {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
        this.clothesRepository = clothesRepository;
    }

    @Override
    public List<DistributionCentres> getAllCentres() {
        String url = "http://localhost:8081/api/distribution-centres";
        HttpHeaders headers = createHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return objectMapper.readValue(
                        response.getBody(),
                        new TypeReference<List<DistributionCentres>>() {});
            } else {
                throw new RuntimeException("Failed to retrieve distribution centres: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error connecting to distribution centre service", e);
        }
    }

    @Override
    public List<DistributionItemDTO> findItemsByNameAndBrand(String name, String brand) {
        String encodedName = UriUtils.encode(name, StandardCharsets.UTF_8);
        String encodedBrand = UriUtils.encode(brand, StandardCharsets.UTF_8);
        String searchUrl = "http://localhost:8081/api/items/search?brand=" + encodedBrand + "&name=" + encodedName;

        HttpHeaders headers = createHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    searchUrl, HttpMethod.GET, request, String.class);

            if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                return new ArrayList<>();
            }

            List<Map<String, Object>> responseItems = objectMapper.readValue(
                    response.getBody(),
                    new TypeReference<List<Map<String, Object>>>() {}
            );

            List<DistributionItemDTO> result = new ArrayList<>();
            for (Map<String, Object> item : responseItems) {
                DistributionItemDTO dto = new DistributionItemDTO();
                dto.setBrand((String) item.get("brand"));
                dto.setName((String) item.get("name"));
                dto.setDistributionCentreName((String) item.get("distributionCentreName"));
                dto.setQuantity(((Number) item.get("quantity")).intValue());

                // Map the centreId and itemId from response
                Map<String, Object> ids = (Map<String, Object>) item.get("ids");
                if (ids != null) {
                    dto.setCentreId(((Number) ids.get("centreId")).longValue());
                    dto.setItemId(((Number) ids.get("itemId")).longValue());
                }

                result.add(dto);
            }

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Error fetching items from distribution centers", e);
        }
    }

    @Override
    public boolean confirmItemRequest(DistributionItemDTO item) {
        // Add item to warehouse
        Clothes newItem = new Clothes();
        newItem.setName(item.getName());
        newItem.setBrand(item.getBrand());
        newItem.setYear(2023); // Default current year
        newItem.setPrice(49.99); // Default price

        clothesRepository.save(newItem);

        // Remove from distribution center
        return removeItemFromDistributionCentre(item.getCentreId(), item.getItemId());
    }

    @Override
    public boolean removeItemFromDistributionCentre(Long centreId, Long itemId) {
        String url = "http://localhost:8081/api/distribution-centres/" + centreId + "/items/" + itemId;
        HttpHeaders headers = createHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.DELETE, request, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            throw new RuntimeException("Failed to remove item from distribution center", e);
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("admin", "admin");
        return headers;
    }
}