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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

class CategoryControllerTest {

    private final String CATEGORY_DESCRIPTION = "Description";
    private final String CATEGORY_ID = "1";

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
        Category category = new Category();
        category.setDescription(CATEGORY_DESCRIPTION);

        Mockito.when(categoryRepository.findById(anyString())).thenReturn(Mono.just(category));

        Category categoryReturned = webTestClient.get()
                .uri(CategoryController.BASE_URL + "/2")
                .exchange()
                .expectBody(Category.class)
                .returnResult().getResponseBody();

        assert categoryReturned != null;
        assertEquals(CATEGORY_DESCRIPTION, categoryReturned.getDescription());
    }

    @Test
    void saveCategory() {
        Category category = new Category();
        category.setDescription(CATEGORY_DESCRIPTION);

        Mockito.when(categoryRepository.save(any())).thenReturn(Mono.just(category));

        Category categorySaved = webTestClient.post()
                .uri(CategoryController.BASE_URL)
                .body(Mono.just(category), Category.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(Category.class)
                .returnResult().getResponseBody();

        assert categorySaved != null;
        assertEquals(CATEGORY_DESCRIPTION, categorySaved.getDescription());
    }

    @Test
    void updateCategory() {
        Category category = new Category();
        category.setDescription(CATEGORY_DESCRIPTION);
        category.setId(CATEGORY_ID);

        Mockito.when(categoryRepository.save(any())).thenReturn(Mono.just(category));

        Category categoryUpdated = webTestClient.put()
                .uri(CategoryController.BASE_URL + "/" + CATEGORY_ID)
                .body(Mono.just(category), Category.class)
                .exchange()
                .expectStatus()
                .isAccepted()
                .expectBody(Category.class).returnResult().getResponseBody();

        assert categoryUpdated != null;
        assertEquals(CATEGORY_ID, categoryUpdated.getId());
        assertEquals(CATEGORY_DESCRIPTION, categoryUpdated.getDescription());
    }
}