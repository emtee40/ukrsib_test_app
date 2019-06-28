package com.semitop7.service;

import com.semitop7.db.entity.Client;
import com.semitop7.db.entity.Transaction;
import com.semitop7.model.TransactionsDto;
import org.springframework.scheduling.annotation.Async;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

public interface FileService {
    boolean isValidFileSize(String fileName, long bytes);

    @Async("threadPoolTaskExecutor")
    Future<TransactionsDto> parseFileAsync(String fileName) throws XMLStreamException, IOException;

    TransactionsDto parseFile(File file) throws XMLStreamException, IOException;

    Set<Client> parseTransactions(List<Transaction> transactions);

    Set<Client> parseTransactions(Collection<Future<TransactionsDto>> transactions);
}