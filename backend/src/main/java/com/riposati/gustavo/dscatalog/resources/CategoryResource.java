package com.riposati.gustavo.dscatalog.resources;

import com.riposati.gustavo.dscatalog.dto.CategoryDTO;
import com.riposati.gustavo.dscatalog.entities.Category;
import com.riposati.gustavo.dscatalog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {

    @Autowired
    CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> findAll() {
        return ResponseEntity.ok().body(categoryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(categoryService.findById(id));
    }

    @PostMapping("/insert")
    public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO categoryDto) {
        categoryDto = categoryService.insert(categoryDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("{id}")
                .buildAndExpand(categoryDto.getId()).toUri();
        return ResponseEntity.created(uri).body(categoryDto);
    }

    @PostMapping("/insert-many-categories")
    public ResponseEntity<List<CategoryDTO>> insert(@RequestBody List<CategoryDTO> categoriesDto) {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .buildAndExpand().toUri();
        return ResponseEntity.created(uri).body(categoryService.insertAll(categoriesDto));
    }
}
