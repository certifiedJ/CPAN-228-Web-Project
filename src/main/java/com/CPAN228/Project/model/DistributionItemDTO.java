package com.CPAN228.Project.model;

import lombok.Data;

@Data
public class DistributionItemDTO {
    private String brand;
    private String name;
    private String distributionCentreName;
    private int quantity;
    private Long centreId;
    private Long itemId;
}