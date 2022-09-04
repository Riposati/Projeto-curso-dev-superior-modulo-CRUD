package com.riposati.gustavo.dscatalog.resources;

import com.riposati.gustavo.dscatalog.dto.ProductDTO;
import com.riposati.gustavo.dscatalog.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDTO>>findAll() {
        return ResponseEntity.ok().body(productService.findAll());
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<ProductDTO>>findAllPaged(Pageable pageable) {
        Page<ProductDTO> list = productService.findAllPaged(pageable);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO>findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(productService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProductDTO>insert(@RequestBody ProductDTO productDto) {
        productDto = productService.insert(productDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("{id}")
                .buildAndExpand(productDto.getId()).toUri();
        return ResponseEntity.created(uri).body(productDto);
    }

    @PostMapping("/insert-many-products")
    public ResponseEntity<List<ProductDTO>>insertManyProducts(@RequestBody List<ProductDTO> productsDto) {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .buildAndExpand().toUri();
        return ResponseEntity.created(uri).body(productService.insertAll(productsDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO>update(@PathVariable("id") Long id, @RequestBody ProductDTO productsDto) {
        return ResponseEntity.ok().body(productService.update(id,productsDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>delete(@PathVariable("id") Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-many-products")
    public ResponseEntity<Void>deleteManyProducts(@RequestBody List<ProductDTO> productDto) {
        productService.deleteMany(productDto);
        return ResponseEntity.noContent().build();
    }
}
