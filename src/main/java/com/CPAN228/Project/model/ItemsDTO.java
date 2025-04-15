package com.CPAN228.Project.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemsDTO {
    private Long id;
    private String brand;
    private String name;
    private int year;
//    private LocalDateTime createdAt;
    private double price;
}
