package com.edoras.spring5webfluxrest.controllers;

import com.edoras.spring5webfluxrest.domain.Category;
import com.edoras.spring5webfluxrest.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

class CategoryControllerTest {

    CategoryController categoryController;

    @Mock
    CategoryRepository categoryRepository;

    WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        categoryController = new CategoryController(categoryRepository);
        webTestClient = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    void getCategories() {
        Mockito.when(categoryRepository.findAll()).thenReturn(Flux.just(new Category(), new Category()));

        webTestClient.get()
                .uri(CategoryController.BASE_URL)
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);
    }

    @Test
    void findCategoryById() {
        String categoryDesc = "Category-Test";
        Category category = new Category();
        category.setDescription(categoryDesc);

        Mockito.when(categoryRepository.findById(anyString())).thenReturn(Mono.just(category));

        Category categoryReturned = webTestClient.get()
                .uri(CategoryController.BASE_URL + "/" + "2")
                .exchange()
                .expectBody(Category.class)
                .returnResult().getResponseBody();

        assert categoryReturned != null;
        assertEquals(categoryDesc, categoryReturned.getDescription());
    }
}