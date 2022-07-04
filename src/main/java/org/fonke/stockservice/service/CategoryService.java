package org.fonke.stockservice.service;


import org.fonke.stockservice.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto save(CategoryDto   categoryDto );

    CategoryDto findById(Integer id);

    CategoryDto  findByCode(String codeCategory);

    List< CategoryDto > findAll();

    void delete(Integer id);
}
