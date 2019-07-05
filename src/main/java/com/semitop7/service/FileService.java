package com.semitop7.service;

import com.semitop7.db.entity.Client;
import com.semitop7.db.entity.Transaction;
import com.semitop7.dto.model.TransactionsDto;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

public interface FileService {
    @Async("threadPoolTaskExecutor")
    Future<TransactionsDto> parseMultipartFileAsync(MultipartFile file) throws XMLStreamException, IOException;

    TransactionsDto parseMultipartFile(MultipartFile file) throws XMLStreamException, IOException;

    Set<Client> parseTransactions(List<Transaction> transactions);

    Set<Client> parseTransactions(Collection<Future<TransactionsDto>> transactions);
}