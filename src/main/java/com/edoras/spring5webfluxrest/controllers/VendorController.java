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

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    Mono<Vendor> updateVendor(@PathVariable String id, @RequestBody Vendor vendor) {
        vendor.setId(id);
        return vendorRepository.save(vendor);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    Mono<Vendor> patchVendor(@PathVariable String id, @RequestBody Vendor vendor) {
        Vendor vendorToPatch = vendorRepository.findById(id).block();

        if (!vendorToPatch.getFirstName().equals(vendor.getFirstName())
                || !vendorToPatch.getLastName().equals(vendor.getLastName())) {
            vendorToPatch.setFirstName(vendor.getFirstName());
            vendorToPatch.setLastName(vendor.getLastName());
            vendorRepository.save(vendorToPatch);
        }
        return Mono.just(vendorToPatch);
    }
}
