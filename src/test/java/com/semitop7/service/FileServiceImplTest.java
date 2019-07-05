package com.semitop7.service;

import com.semitop7.db.entity.Client;
import com.semitop7.db.entity.Transaction;
import com.semitop7.dto.model.TransactionsDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

import static com.semitop7.ObjectBuilderTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileServiceImplTest {
    @Autowired
    private FileService fileService;

    @Test
    public void testAssert() {
        assertThat(fileService).isNotNull();
    }

    @Test
    public void parseEmptyTransactions() {
        Set<Client> clients = fileService.parseTransactions(new LinkedList<Transaction>());
        assertNotNull(clients);
        assertEquals(0, clients.size());
    }

    @Test
    public void parseEmptyFutureTransactions() {
        Set<Client> clients = fileService.parseTransactions(new LinkedList<Future<TransactionsDto>>());
        assertNotNull(clients);
        assertEquals(0, clients.size());
    }

    @Test
    public void parseTransactions() {
        List<Transaction> list = createTransactions(123456789L, 123456789L, 987654321L, 987654321L);
        Set<Client> clients = fileService.parseTransactions(list);

        assertNotNull(clients);
        assertFalse(CollectionUtils.isEmpty(clients));
        assertEquals(2, clients.size());
    }

    @Test
    public void parseFutureTransactions() {
        List<Future<TransactionsDto>> list
                = createFutureTransactions(123456789L, 123456789L, 987654321L, 987654321L);
        Set<Client> clients = fileService.parseTransactions(list);

        assertNotNull(clients);
        assertFalse(CollectionUtils.isEmpty(clients));
        assertEquals(2, clients.size());
    }

    @Test
    public void parseMultipartFile() throws IOException, XMLStreamException {
        MockMultipartFile file = new MockMultipartFile(
                "files[]",
                "test.xml",
                "text/xml",
                testXmlFile().getBytes());
        TransactionsDto dto = fileService.parseMultipartFile(file);

        assertNotNull(dto);
        assertNotNull(dto.getTransactions());
        assertFalse(CollectionUtils.isEmpty(dto.getTransactions()));
        assertNotNull(dto.getTransactions().get(0));
    }
}