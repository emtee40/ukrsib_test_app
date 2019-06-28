package com.semitop7.service.db.impl;

import com.semitop7.db.entity.Transaction;
import com.semitop7.db.repository.TransactionRepository;
import com.semitop7.model.TransactionsDto;
import com.semitop7.service.db.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public List<Transaction> saveAll(TransactionsDto transactions) {
        if (transactions != null && !CollectionUtils.isEmpty(transactions.getTransactions())) {
            return transactionRepository.saveAll(transactions.getTransactions());
        }
        return new LinkedList<>();
    }

    @Override
    public List<Transaction> saveAll(List<Transaction> transactions) {
        if (!CollectionUtils.isEmpty(transactions)) {
            return transactionRepository.saveAll(transactions);
        }
        return new LinkedList<>();
    }
}
