package org.example;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@CacheConfig(cacheNames = "customers")
public class CustomerDataService {
    private final CustomerRepository customerRepository;

    public CustomerDataService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Cacheable(key = "#id")
    public Optional<Customer> getCustomer(Long id) {
        return customerRepository.findOneById(id);
    }

//    @CacheEvict(key = "#customer.id")
    @CachePut(key = "#customer.id")
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }
}
