package com.riposati.gustavo.dscatalog.repositories;

import com.riposati.gustavo.dscatalog.entities.Product;
import com.riposati.gustavo.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import java.util.Optional;
@DataJpaTest
public class ProductRepositoryTests {

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;

    @Autowired
    private ProductRepository repository;

    @BeforeEach
    void setup() throws Exception{
        this.existingId = 1L;
        this.nonExistingId = 1000L;
        this.countTotalProducts = 25L;
    }

    @Test
    void ShoudDeleteObjectWhenIdExists(){
        repository.deleteById(this.existingId);
        Optional<Product> response = repository.findById(existingId);
        Assertions.assertFalse(response.isPresent());
    }

    @Test
    void ShoudDeleteObjectWhenIdDoesNotExist(){
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            repository.deleteById(this.nonExistingId);
        });
    }

    @Test
    void saveShoudPersistWithAutoincrementWhenIdIsNull(){
        Product product = Factory.createProduct();
        product.setId(null);
        product = repository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProducts + 1, product.getId());
    }
}
