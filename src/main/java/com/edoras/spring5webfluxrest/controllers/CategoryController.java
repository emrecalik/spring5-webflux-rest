package com.edoras.spring5webfluxrest.controllers;

import com.edoras.spring5webfluxrest.domain.Category;
import com.edoras.spring5webfluxrest.repositories.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping({CategoryController.BASE_URL})
public class CategoryController {

    public static final String BASE_URL = "/api/v1/categories";

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    Flux<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @GetMapping("/{id}")
    Mono<Category> findCategoryById(@PathVariable String id) {
        return categoryRepository.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Mono<Category> saveCategory(@RequestBody Category category) {
        return categoryRepository.save(category);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    Mono<Category> updateCategory(@PathVariable String id, @RequestBody Category category) {
        category.setId(id);
        return categoryRepository.save(category);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    Mono<Category> patchCategory(@PathVariable String id, @RequestBody Category category) {
        Category categoryToPatch = categoryRepository.findById(id).block();

        if (!categoryToPatch.getDescription().equals(category.getDescription())) {
            categoryToPatch.setDescription(category.getDescription());
            return categoryRepository.save(categoryToPatch);
        }
        return Mono.just(categoryToPatch);
    }
}
