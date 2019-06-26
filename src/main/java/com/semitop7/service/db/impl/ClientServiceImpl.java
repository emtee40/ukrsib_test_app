package com.semitop7.service.db.impl;

import com.semitop7.db.entity.Client;
import com.semitop7.db.repository.ClientRepository;
import com.semitop7.service.db.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<Client> saveAll(Collection<Client> clients) {
        if (!CollectionUtils.isEmpty(clients)) {
            return clientRepository.saveAll(clients);
        }
        return new LinkedList<>();
    }

    @Override
    public Client save(Client client) {
        return clientRepository.save(client);
    }
}
