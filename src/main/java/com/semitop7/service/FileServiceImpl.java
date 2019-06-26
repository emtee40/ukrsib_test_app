package com.semitop7.service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.semitop7.db.entity.Client;
import com.semitop7.db.entity.Transaction;
import com.semitop7.dto.model.TransactionsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
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
    @Async("threadPoolTaskExecutor")
    public Future<TransactionsDto> parseMultipartFileAsync(MultipartFile file) throws IOException, XMLStreamException {
        TransactionsDto dto = parseMultipartFile(file);
        return new AsyncResult<>(dto);
    }

    @Override
    public TransactionsDto parseMultipartFile(MultipartFile file) throws XMLStreamException, IOException {
        LOGGER.info(String.format("[FILE] name: %s, size (bytes): %d",
                file.getOriginalFilename(),
                file.getSize()));
        InputStream fileStream = file.getInputStream();
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
        }
        return null;
    }

    @Override
    public Set<Client> parseTransactionsFuture(List<Transaction> transactions) {
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

    @Override
    public Set<Client> parseTransactionsFuture(Collection<Future<TransactionsDto>> transactions) {
        return transactions.parallelStream().flatMap(ts -> {
            try {
                TransactionsDto dto = ts.get();
                if (dto != null) {
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
}
