package com.riposati.gustavo.dscatalog.entities;

import com.riposati.gustavo.dscatalog.repositories.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    ProductRepository repository;

    @Test
    void ShoudDeleteObjectWhenIdExists(){
        Long existingId = 1L;
        repository.deleteById(existingId);
        Optional<Product> response = repository.findById(existingId);
        Assertions.assertFalse(response.isPresent());
    }
}
