package com.semitop7.service.db;

import com.semitop7.db.entity.Transaction;
import com.semitop7.model.TransactionsDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TransactionService {
    @Transactional
    List<Transaction> saveAll(TransactionsDto transactions);

    List<Transaction> saveAll(List<Transaction> transactions);
}
