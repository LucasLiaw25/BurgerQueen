package com.liaw.dev.BurgerQueen.dto.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.liaw.dev.BurgerQueen.entity.Product;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Long id;
    private String name;
    private String slug;
}
