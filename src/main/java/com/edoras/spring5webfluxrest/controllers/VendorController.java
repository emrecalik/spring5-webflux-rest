package com.edoras.spring5webfluxrest.controllers;

import com.edoras.spring5webfluxrest.domain.Vendor;
import com.edoras.spring5webfluxrest.repositories.VendorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping({VendorController.BASE_URL})
public class VendorController {

    public static final String BASE_URL = "/api/v1/vendors";

    private final VendorRepository vendorRepository;

    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @GetMapping
    Flux<Vendor> getVendors() {
        return vendorRepository.findAll();
    }

    @GetMapping("/{id}")
    Mono<Vendor> findVendorById(@PathVariable String id) {
        return vendorRepository.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    Mono<Vendor> saveVendor(@RequestBody Vendor vendor) {
        return vendorRepository.save(vendor);
    }
}
