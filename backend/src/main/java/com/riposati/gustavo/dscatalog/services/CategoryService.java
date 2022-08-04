package com.riposati.gustavo.dscatalog.services;

import com.riposati.gustavo.dscatalog.dto.CategoryDTO;
import com.riposati.gustavo.dscatalog.entities.Category;
import com.riposati.gustavo.dscatalog.repositories.CategoryRepository;
import com.riposati.gustavo.dscatalog.services.exceptions.DatabaseException;
import com.riposati.gustavo.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
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
    public Page<CategoryDTO> findAllPaged(PageRequest pageRequest){
        Page<Category> categories = categoryRepository.findAll(pageRequest);
        return categories.map(CategoryDTO::new);
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Optional<Category> obj = categoryRepository.findById(id);
        Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
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

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO categoriesDto) {

        try{
            Category entity = categoryRepository.getOne(id);
            entity.setName(categoriesDto.getName());
            entity = categoryRepository.save(entity);
            return new CategoryDTO(entity);

        }catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }

    public void delete(Long id) {
        try{
            categoryRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Id not found " + id);
        }
        catch (DataIntegrityViolationException e){
            throw new DatabaseException("Integrity violation");
        }
    }

    public void deleteMany(List<CategoryDTO>categoriesDto) {
        try{
            List<Category> categories = categoriesDto.stream().map(x -> new Category(x.getId(), x.getName())).collect(Collectors.toList());
            categoryRepository.deleteAll(categories);
        }
        catch (DataIntegrityViolationException e){
            throw new DatabaseException("Integrity violation");
        }
    }
}
