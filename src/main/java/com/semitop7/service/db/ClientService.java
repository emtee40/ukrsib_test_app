package com.semitop7.service.db;

import com.semitop7.db.entity.Client;

import java.util.Collection;
import java.util.List;

public interface ClientService {
    List<Client> saveAll(Collection<Client> clients);

    Client save(Client client);
}