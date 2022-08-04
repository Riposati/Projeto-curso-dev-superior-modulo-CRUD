package com.riposati.gustavo.dscatalog.resources;

import com.riposati.gustavo.dscatalog.dto.CategoryDTO;
import com.riposati.gustavo.dscatalog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>>findAll() {
        return ResponseEntity.ok().body(categoryService.findAll());
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<CategoryDTO>>findAllPaged(

            @RequestParam(value="page", defaultValue = "0") Integer page,
            @RequestParam(value="linesPerPage", defaultValue = "12") Integer linesPerPage,
            @RequestParam(value="direction", defaultValue = "ASC") String direction,
            @RequestParam(value="orderBy", defaultValue = "name") String orderBy

    ) {
        PageRequest pageRequest = PageRequest.of(page,linesPerPage, Sort.Direction.valueOf(direction),orderBy);

        Page<CategoryDTO> list = categoryService.findAllPaged(pageRequest);

        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO>findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(categoryService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CategoryDTO>insert(@RequestBody CategoryDTO categoryDto) {
        categoryDto = categoryService.insert(categoryDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("{id}")
                .buildAndExpand(categoryDto.getId()).toUri();
        return ResponseEntity.created(uri).body(categoryDto);
    }

    @PostMapping("/insert-many-categories")
    public ResponseEntity<List<CategoryDTO>>insertManyCategories(@RequestBody List<CategoryDTO> categoriesDto) {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .buildAndExpand().toUri();
        return ResponseEntity.created(uri).body(categoryService.insertAll(categoriesDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO>update(@PathVariable("id") Long id, @RequestBody CategoryDTO categoriesDto) {
        return ResponseEntity.ok().body(categoryService.update(id,categoriesDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>delete(@PathVariable("id") Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-many-categories")
    public ResponseEntity<Void>deleteManyCategories(@RequestBody List<CategoryDTO> categoriesDto) {
        categoryService.deleteMany(categoriesDto);
        return ResponseEntity.noContent().build();
    }
}
