package com.edoras.spring5webfluxrest.bootstrap;

import com.edoras.spring5webfluxrest.domain.Category;
import com.edoras.spring5webfluxrest.domain.Vendor;
import com.edoras.spring5webfluxrest.repositories.CategoryRepository;
import com.edoras.spring5webfluxrest.repositories.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements CommandLineRunner {

    private final VendorRepository vendorRepository;
    private final CategoryRepository categoryRepository;

    public Bootstrap(VendorRepository vendorRepository, CategoryRepository categoryRepository) {
        this.vendorRepository = vendorRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (vendorRepository.count().block() == 0) {
            System.out.println("###Vendors loading..###");
            loadVendors();
        }
        if (categoryRepository.count().block() == 0) {
            System.out.println("###Categories loading..###");
            loadCategories();
        }
    }

    private void loadVendors() {
        vendorRepository.save(
                Vendor.builder()
                        .firstName("Vendor-1 First Name")
                        .lastName("Vendor-1 Last Nmae")
                        .build())
                .block();

        vendorRepository.save(
                Vendor.builder()
                        .firstName("Vendor-2 First Name")
                        .lastName("Vendor-2 Last Nmae")
                        .build())
                .block();

        System.out.println("Vendors Loaded = #" + vendorRepository.count().block());
    }

    private void loadCategories() {
        categoryRepository.save(
                Category.builder()
                        .description("Category-1")
                        .build())
                .block();

        categoryRepository.save(
                Category.builder()
                        .description("Category-2")
                        .build())
                .block();

        categoryRepository.save(
                Category.builder()
                        .description("Category-2")
                        .build())
                .block();

        System.out.println("Categories Loaded = #" + categoryRepository.count().block());
    }
}
