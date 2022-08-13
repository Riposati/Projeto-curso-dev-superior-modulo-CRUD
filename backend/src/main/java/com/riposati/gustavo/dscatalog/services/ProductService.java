package com.riposati.gustavo.dscatalog.services;

import com.riposati.gustavo.dscatalog.dto.CategoryDTO;
import com.riposati.gustavo.dscatalog.dto.ProductDTO;
import com.riposati.gustavo.dscatalog.entities.Category;
import com.riposati.gustavo.dscatalog.entities.Product;
import com.riposati.gustavo.dscatalog.repositories.CategoryRepository;
import com.riposati.gustavo.dscatalog.repositories.ProductRepository;
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
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<ProductDTO> findAll(){
        List<Product> products = productRepository.findAll();
        return products.stream().map(ProductDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(PageRequest pageRequest){
        Page<Product> products = productRepository.findAll(pageRequest);
        return products.map(ProductDTO::new);
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> obj = productRepository.findById(id);
        Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new ProductDTO(entity, entity.getCategories());
    }

    @Transactional
    public ProductDTO insert(ProductDTO productDto){
        Product entity = new Product();
        addNewProductAndCategories(productDto, entity);
        entity = productRepository.save(entity);
        return new ProductDTO(entity);
    }

    @Transactional
    public List<ProductDTO> insertAll(List<ProductDTO> productsDto) {
        List<Product> products = productsDto.stream().map(x -> new Product(x.getId(), x.getName(), x.getDescription(), x.getPrice(), x.getImgUrl(), x.getDate())).collect(Collectors.toList());
        products = productRepository.saveAll(products);
        productsDto = products.stream().map(x -> new ProductDTO(x.getId(), x.getName(), x.getDescription(), x.getPrice(), x.getImgUrl(), x.getDate())).collect(Collectors.toList());
        return productsDto;
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO productDto) {

        try{
            Product entity = productRepository.getOne(id);
            addNewProductAndCategories(productDto, entity);
            entity = productRepository.save(entity);
            return new ProductDTO(entity);

        }catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }


    public void delete(Long id) {
        try{
            productRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Id not found " + id);
        }
        catch (DataIntegrityViolationException e){
            throw new DatabaseException("Integrity violation");
        }
    }

    public void deleteMany(List<ProductDTO>productsDto) {
        try{
            List<Product> products = productsDto.stream().map(x -> new Product(x.getId(), x.getName(), x.getDescription(), x.getPrice(), x.getImgUrl(), x.getDate())).collect(Collectors.toList());
            productRepository.deleteAll(products);
        }
        catch (DataIntegrityViolationException e){
            throw new DatabaseException("Integrity violation");
        }
    }
    private void addNewProductAndCategories(ProductDTO productDto, Product entity) {

        entity.setName(productDto.getName());
        entity.setDescription(productDto.getDescription());
        entity.setDate(productDto.getDate());
        entity.setImgUrl(productDto.getImgUrl());
        entity.setPrice(productDto.getPrice());
        entity.setDate(productDto.getDate());

        entity.getCategories().clear();
        for(CategoryDTO catDto : productDto.getCategories()){
            Category category = categoryRepository.getOne(catDto.getId());
            entity.getCategories().add(category);
        }
    }
}
