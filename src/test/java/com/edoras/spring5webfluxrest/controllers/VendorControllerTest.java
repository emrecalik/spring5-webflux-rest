package com.edoras.spring5webfluxrest.controllers;

import com.edoras.spring5webfluxrest.domain.Vendor;
import com.edoras.spring5webfluxrest.repositories.VendorRepository;
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

class VendorControllerTest {

    private final String VENDOR_NAME = "Emre";
    private final String VENDOR_ID = "1";

    VendorController vendorController;

    @Mock
    VendorRepository vendorRepository;

    WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        vendorController = new VendorController(vendorRepository);
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    void getVendors() {
        Mockito.when(vendorRepository.findAll()).thenReturn(Flux.just(new Vendor(), new Vendor()));

        webTestClient.get()
                .uri(VendorController.BASE_URL)
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    void findVendorById() {
        Vendor vendor = new Vendor();
        vendor.setFirstName(VENDOR_NAME);

        Mockito.when(vendorRepository.findById(anyString())).thenReturn(Mono.just(vendor));

        Vendor vendorReturned = webTestClient.get()
                .uri(VendorController.BASE_URL + "/2")
                .exchange()
                .expectBody(Vendor.class)
                .returnResult().getResponseBody();

        assertEquals(VENDOR_NAME, vendorReturned.getFirstName());
    }

    @Test
    void saveVendor() {
        Vendor vendor = new Vendor();
        vendor.setFirstName(VENDOR_NAME);

        Mockito.when(vendorRepository.save(any())).thenReturn(Mono.just(vendor));

        Vendor vendorReturned = webTestClient.post()
                .uri(VendorController.BASE_URL)
                .body(Mono.just(vendor), Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(Vendor.class).returnResult().getResponseBody();

        assertEquals(VENDOR_NAME, vendorReturned.getFirstName());
    }

    @Test
    void updateVendor() {
        Vendor vendor = new Vendor();
        vendor.setFirstName(VENDOR_NAME);
        vendor.setId(VENDOR_ID);

        Mockito.when(vendorRepository.save(any())).thenReturn(Mono.just(vendor));

        Vendor vendorUpdated = webTestClient.put()
                .uri(VendorController.BASE_URL + "/" + VENDOR_ID)
                .body(Mono.just(new Vendor()), Vendor.class)
                .exchange()
                .expectStatus()
                .isAccepted()
                .expectBody(Vendor.class).returnResult().getResponseBody();

        assert vendorUpdated != null;
        assertEquals(VENDOR_NAME, vendorUpdated.getFirstName());
    }
}