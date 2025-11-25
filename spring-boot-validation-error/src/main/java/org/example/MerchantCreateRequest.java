package org.example;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;

public class MerchantCreateRequest {
    @UUID
    @NotNull
    private String partner_id;

    @NotNull
    private String merchant_name;

    public MerchantCreateRequest() {
    }

    public MerchantCreateRequest(String partner_id, String merchant_name) {
        this.partner_id = partner_id;
        this.merchant_name = merchant_name;
    }

    public String getPartner_id() {
        return partner_id;
    }

    public String getMerchant_name() {
        return merchant_name;
    }
}
