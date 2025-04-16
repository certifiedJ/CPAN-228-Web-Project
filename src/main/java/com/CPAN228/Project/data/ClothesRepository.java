package com.CPAN228.Project.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.CPAN228.Project.model.Clothes;

import java.util.List;
import java.util.Optional;

public interface ClothesRepository extends JpaRepository<Clothes, Long> {
    List<Clothes> findByBrandAndYear(String brand, int year);

    Page<Clothes> findAll(Pageable pageable);

    Page<Clothes> findAllByOrderByBrandAsc(Pageable pageable);

    Optional<Clothes> findByBrandAndName(String brand, String name);
}
