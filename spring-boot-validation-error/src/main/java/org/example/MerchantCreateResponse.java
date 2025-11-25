package org.example;

public class MerchantCreateResponse {
    private final String merchantId;
    private final String partner_id;
    private final String merchant_name;

    public MerchantCreateResponse(String merchantId, String partner_id, String merchant_name) {
        this.merchantId = merchantId;
        this.partner_id = partner_id;
        this.merchant_name = merchant_name;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getPartner_id() {
        return partner_id;
    }

    public String getMerchant_name() {
        return merchant_name;
    }
}
