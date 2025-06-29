package com.morzevichka.manageyourstore.dto;

import java.sql.Timestamp;

public class OrderClientDto {
    private Long id;
    private Long workerId;
    private String name;
    private Timestamp purchaseDate;
    private Double totalPrice;

    public OrderClientDto(Long id, Long workerId, String name, Timestamp purchaseDate, Double totalPrice) {
        this.id = id;
        this.workerId = workerId;
        this.name = name;
        this.purchaseDate = purchaseDate;
        this.totalPrice = totalPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Long workerId) {
        this.workerId = workerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Timestamp purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
