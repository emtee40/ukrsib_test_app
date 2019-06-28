package com.semitop7.service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.semitop7.db.entity.Client;
import com.semitop7.db.entity.Transaction;
import com.semitop7.model.TransactionsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileServiceImpl implements FileService {
    private final String TRANSACTIONS_KEY = "transactions";
    private final Logger LOGGER = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    private XmlMapper xmlMapper;

    @Override
    public boolean isValidFileSize(String fileName, long bytes) {
        File file = new File(fileName);
        return file.length() < bytes;
    }

    @Override
    @Async("threadPoolTaskExecutor")
    public Future<TransactionsDto> parseFileAsync(String fileName) throws XMLStreamException, IOException {
        File file = new File(fileName);
        TransactionsDto dto = parseFile(file);
        return new AsyncResult<>(dto);
    }

    @Override
    public TransactionsDto parseFile(File file) throws XMLStreamException, IOException {
        LOGGER.info(String.format("[FILE] name: %s, size (bytes): %d",
                file.getName(),
                file.length()));
        InputStream fileStream = new FileInputStream(file);
        XMLStreamReader sr = XMLInputFactory.newFactory().createXMLStreamReader(fileStream);
        try {
            while (sr.hasNext()) {
                if (sr.isStartElement() && TRANSACTIONS_KEY.equals(sr.getLocalName())) {
                    return xmlMapper.readValue(sr, TransactionsDto.class);
                }
                sr.next();
            }

        } finally {
            sr.close();
            fileStream.close();
        }
        return null;
    }

    @Override
    public Set<Client> parseTransactions(List<Transaction> transactions) {
        if (!CollectionUtils.isEmpty(transactions)) {
            return transactions
                    .parallelStream()
                    .map(t -> {
                        Client c = t.getClient();
                        if (c != null) {
                            c.addTransaction(t);
                        }
                        return c;
                    }).collect(Collectors.toSet());
        }
        return new LinkedHashSet<>();
    }

    @Override
    public Set<Client> parseTransactions(Collection<Future<TransactionsDto>> transactions) {
        if (!CollectionUtils.isEmpty(transactions)) {
            return transactions.parallelStream().flatMap(ts -> {
                try {
                    TransactionsDto dto = ts.get();
                    if (dto != null && !CollectionUtils.isEmpty(dto.getTransactions())) {
                        return dto.getTransactions().parallelStream();
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                return Stream.empty();
            }).map(t -> {
                Client c = t.getClient();
                if (c != null) {
                    c.addTransaction(t);
                }
                return c;
            }).collect(Collectors.toSet());
        }
        return new LinkedHashSet<>();
    }
}
