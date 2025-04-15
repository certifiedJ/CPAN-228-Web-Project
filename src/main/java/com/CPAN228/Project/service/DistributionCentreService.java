package com.CPAN228.Project.service;

import com.CPAN228.Project.model.DistributionCentres; // Corrected class name
import java.util.List;

public interface DistributionCentreService {
    List<DistributionCentres> getAllCentres();
    boolean requestItem(String name, String brand);
}