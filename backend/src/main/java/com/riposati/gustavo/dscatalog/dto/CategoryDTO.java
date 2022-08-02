package com.riposati.gustavo.dscatalog.dto;

import com.riposati.gustavo.dscatalog.entities.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CategoryDTO {
    private Long id;
    private String name;

    public CategoryDTO(Category category){
        this.id = category.getId();
        this.name = category.getName();
    }

}
