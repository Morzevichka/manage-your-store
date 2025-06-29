package com.morzevichka.manageyourstore.dto;

public class ProductSalesDto {
    private String name;
    private Double price;
    private Double totalPrice;

    public ProductSalesDto(String name, Double price, Double totalPrice) {
        this.name = name;
        this.price = price;
        this.totalPrice = totalPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
