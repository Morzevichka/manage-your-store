package com.morzevichka.manageyourstore.dto;

public class WorkerSalesDto {
    private Long id;
    private String name;
    private String username;
    private String role;
    private Double sumSales;

    public WorkerSalesDto(Long id, String name, String username, String role, Double sumSales) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.role = role;
        this.sumSales = sumSales;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Double getSumSales() {
        return sumSales;
    }

    public void setSumSales(Double sumSales) {
        this.sumSales = sumSales;
    }
}
