package com.riposati.gustavo.dscatalog.services;

import com.riposati.gustavo.dscatalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    public List findAll(){
        return categoryRepository.findAll();
    }
}
