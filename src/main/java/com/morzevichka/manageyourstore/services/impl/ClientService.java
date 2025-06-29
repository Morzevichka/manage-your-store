package com.morzevichka.manageyourstore.services.impl;

import com.morzevichka.manageyourstore.entity.Client;

import java.util.List;

public interface ClientService {

    Client findClient(Long id);

    void saveClient(Client client);

    void updateClient(Client client);

    void removeClient(Client client);

    List<Client> findAllClients();

    Client findClientByPhone(String phone);
}
