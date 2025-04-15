package com.CPAN228.Project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Clothes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdAt = LocalDateTime.now();
    private String name;
    private String brand;
    @Column(name = "product_year")
    private int year;
    private double price;
    private int quantity;

    public Clothes(Long id, String name, String brand, int year, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.year = year;
        this.price = price;
        this.quantity = quantity;
    }
}