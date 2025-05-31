package org.example.productbatch.model;

import java.math.BigDecimal;

public class AcmeProduct {
    private String index;
    private String name;
    private String description;
    private String brand;
    private String category;
    private BigDecimal price;
    private String currency;
    private Integer stock;
    private String ean;
    private String color;
    private String size;
    private String availability;
    private String internalId;

    public AcmeProduct() {}

    public AcmeProduct(String index, String name, String description, String brand, String category, BigDecimal price, String currency, Integer stock, String ean, String color, String size, String availability, String internalId) {
        this.index = index;
        this.name = name;
        this.description = description;
        this.brand = brand;
        this.category = category;
        this.price = price;
        this.currency = currency;
        this.stock = stock;
        this.ean = ean;
        this.color = color;
        this.size = size;
        this.availability = availability;
        this.internalId = internalId;
    }

    public String getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getBrand() {
        return brand;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public Integer getStock() {
        return stock;
    }

    public String getEan() {
        return ean;
    }

    public String getColor() {
        return color;
    }

    public String getSize() {
        return size;
    }

    public String getAvailability() {
        return availability;
    }

    public String getInternalId() {
        return internalId;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }
}