package com.riposati.gustavo.dscatalog.services;

import com.riposati.gustavo.dscatalog.dto.CategoryDTO;
import com.riposati.gustavo.dscatalog.entities.Category;
import com.riposati.gustavo.dscatalog.repositories.CategoryRepository;
import com.riposati.gustavo.dscatalog.services.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll(){
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(CategoryDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Optional<Category> obj = categoryRepository.findById(id);
        Category entity = obj.orElseThrow(() -> new EntityNotFoundException("Entity not found"));
        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO categoryDto){
        Category entity = new Category();
        entity.setName(categoryDto.getName());
        entity = categoryRepository.save(entity);
        return new CategoryDTO(entity);
    }

    @Transactional
    public List<CategoryDTO> insertAll(List<CategoryDTO> categoriesDto) {
        List<Category> categories = categoriesDto.stream().map(x -> new Category(x.getId(), x.getName())).collect(Collectors.toList());
        categories = categoryRepository.saveAll(categories);
        categoriesDto = categories.stream().map(x -> new CategoryDTO(x.getId(), x.getName())).collect(Collectors.toList());
        return categoriesDto;
    }
}
