package org.example;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MerchantController {
    @PostMapping("/merchants")
    public MerchantCreateResponse createMerchant(@Valid @RequestBody MerchantCreateRequest request) {
        // TODO: To be implemented
        return null;
    }
}
