package com.morzevichka.manageyourstore.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "CARDS")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    private BigDecimal cashback;

    @OneToMany(mappedBy = "card", fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Client> clients;

    public Card() {}

    public Card(Long id, String name, BigDecimal cashback, Set<Client> clients) {
        this.id = id;
        this.name = name;
        this.cashback = cashback;
        this.clients = clients;
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

    public BigDecimal getCashback() {
        return cashback;
    }

    public void setCashback(BigDecimal cashback) {
        this.cashback = cashback;
    }

    public Set<Client> getClients() {
        return clients;
    }

    public void setClients(Set<Client> clients) {
        this.clients = clients;
    }
}
