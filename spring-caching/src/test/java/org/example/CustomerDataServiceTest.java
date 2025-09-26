package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = Main.class)
@TestPropertySource(properties = {
        "app.cache.enabled=true",
})
class CustomerDataServiceTest {
    @Autowired
    private CacheManager cacheManager;

    @MockitoSpyBean
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerDataService customerDataService;

    @BeforeEach
    public void setUp() {
        Cache cache = cacheManager.getCache("customers");
        if (cache != null) {
            cache.clear();
        }
    }

    @Test
    public void getCustomer_should_fill_the_cache_after_the_first_call() {
        assertThat(getCachedCustomer(2L)).isEmpty();

        Customer customer = new Customer(2L, "Alex Smith", "alex.smith@test.net");
        doReturn(Optional.of(customer)).when(customerRepository).findOneById(2L);

        Optional<Customer> firstCall = customerDataService.getCustomer(2L);
        verify(customerRepository, times(1)).findOneById(2L);
        assertThat(firstCall).contains(customer);

        assertThat(getCachedCustomer(2L)).isNotEmpty();
    }

    @Test
    public void getCustomer_should_return_cached_value_on_second_call() {
        Customer customer = new Customer(2L, "Alex Smith", "alex.smith@test.net");
        doReturn(Optional.of(customer)).when(customerRepository).findOneById(2L);

        Optional<Customer> firstCall = customerDataService.getCustomer(2L);
        Optional<Customer> secondCall = customerDataService.getCustomer(2L);

        verify(customerRepository, times(1)).findOneById(2L);

        assertThat(firstCall)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(customer);

        assertThat(secondCall)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(customer);
    }

    @Test
    public void save_should_evict_entry_from_cache() {
        Customer customer = new Customer(2L, "Alex Smith", "alex.smith@test.net");
        Customer modifiedCustomer = new Customer(2L, "Alex Smith Jr.", "alex.smith@test.net");

        when(customerRepository.findOneById(2L))
                .thenReturn(Optional.of(customer))
                .thenReturn(Optional.of(modifiedCustomer));

        when(customerRepository.save(modifiedCustomer))
                .thenReturn(modifiedCustomer);

        // First call fills the cache
        customerDataService.getCustomer(2L);

        // cache must be filled
        assertThat(getCachedCustomer(2L)).isNotEmpty();

        // Save the modified entity
        customerDataService.save(modifiedCustomer);

        // cache must be empty after update
        assertThat(getCachedCustomer(2L)).isEmpty();

        Optional<Customer> customerAfterUpdate = customerDataService.getCustomer(2L);

        // findOneById()  should be called twice,  before and after update
        verify(customerRepository, times(2)).findOneById(2L);

        // getCustomer() should return updated value
        assertThat(customerAfterUpdate)
                .get()
                .usingRecursiveAssertion()
                .isEqualTo(modifiedCustomer);
    }

    @Test
    public void save_should_update_entry_in_cache() {
        Customer customer = new Customer(2L, "Alex Smith", "alex.smith@test.net");
        Customer modifiedCustomer = new Customer(2L, "Alex Smith Jr.", "alex.smith@test.net");

        when(customerRepository.findOneById(2L))
                .thenReturn(Optional.of(customer))
                .thenReturn(Optional.of(modifiedCustomer));

        when(customerRepository.save(modifiedCustomer))
                .thenReturn(modifiedCustomer);

        // First call fills the cache
        customerDataService.getCustomer(2L);

        // cache must be filled
        assertThat(getCachedCustomer(2L)).isNotEmpty();

        // Save the modified entity
        Customer updatedCustomer = customerDataService.save(modifiedCustomer);

        // cache must be updated with the new value
        assertThat(getCachedCustomer(2L))
                .get()
                .usingRecursiveComparison()
                .isEqualTo(updatedCustomer);
    }

    private Optional<Customer> getCachedCustomer(Long id) {
        return Optional.ofNullable(cacheManager.getCache("customers"))
                .map(c -> c.get(id, Customer.class));
    }
}