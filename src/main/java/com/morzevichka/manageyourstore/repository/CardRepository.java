package com.morzevichka.manageyourstore.repository;

import com.morzevichka.manageyourstore.annotation.Query;
import com.morzevichka.manageyourstore.entity.Card;
import com.morzevichka.manageyourstore.repository.impl.GenericRepository;

import java.util.Optional;

public class CardRepository extends GenericRepository<Card, Long> {

    public CardRepository() {
        super(Card.class);
    }

    @Query(value = "SELECT * FROM CARDS WHERE NAME = ?1")
    public Optional<Card> findByName(String name) {
        return getSingleResultQuery(name);
    }
}
