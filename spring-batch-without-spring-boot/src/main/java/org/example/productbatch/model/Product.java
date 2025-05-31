package org.example.productbatch.model;

import java.math.BigDecimal;

public class Product {
    private String name;

    private String description;

    private BigDecimal price;

    private String currency;

    public Product() {
    }

    public Product(String name, String description, BigDecimal price, String currency) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.currency = currency;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }
}
