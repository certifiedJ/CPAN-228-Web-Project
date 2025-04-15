package com.CPAN228.Project.service;

import com.CPAN228.Project.model.DistributionCentres;
import com.CPAN228.Project.model.DistributionItemDTO;

import java.util.List;

public interface DistributionCentreService {
    List<DistributionCentres> getAllCentres();
    List<DistributionItemDTO> findItemsByNameAndBrand(String name, String brand);
    boolean confirmItemRequest(DistributionItemDTO item);
    boolean removeItemFromDistributionCentre(Long centreId, Long itemId);
}