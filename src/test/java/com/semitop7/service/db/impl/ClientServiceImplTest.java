package com.semitop7.service.db.impl;

import com.semitop7.db.entity.Client;
import com.semitop7.db.repository.ClientRepository;
import com.semitop7.service.db.ClientService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.semitop7.ObjectBuilderTest.createClients;
import static com.semitop7.ObjectBuilderTest.createOneClient;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClientServiceImplTest {
    @Autowired
    private ClientService clientService;

    @MockBean
    private ClientRepository clientRepository;

    @Test
    public void testAssert() {
        assertThat(clientService).isNotNull();
    }

    @Test
    public void saveAll() {
        List<Client> clients = createClients(123456789L, 123456789L, 987654321L);
        clientService.saveAll(clients);

        Mockito.verify(clientRepository, Mockito.times(1)).saveAll(clients);
    }

    @Test
    public void save() {
        Client client = createOneClient(123456789L);
        clientService.save(client);

        Mockito.verify(clientRepository, Mockito.times(1)).save(client);
    }
}