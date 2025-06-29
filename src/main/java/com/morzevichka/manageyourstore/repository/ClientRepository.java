package com.morzevichka.manageyourstore.repository;

import com.morzevichka.manageyourstore.annotation.Query;
import com.morzevichka.manageyourstore.entity.Client;
import com.morzevichka.manageyourstore.repository.impl.GenericRepository;

import java.util.Optional;

public class ClientRepository extends GenericRepository<Client, Long> {

    public ClientRepository() {
        super(Client.class);
    }

    @Query(value = "SELECT c FROM Client c WHERE c.phone = ?1", nativeQuery = false)
    public Optional<Client> findByPhoneNumber(String phone) {
        return getSingleResultQuery(phone);
    }
}
