package com.CPAN228.Project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistributionCentres {
    private Long id;
    private String name;
    private double latitude;
    private double longitude;
}