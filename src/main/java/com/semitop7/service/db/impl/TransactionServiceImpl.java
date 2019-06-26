package com.semitop7.service.db.impl;

import com.semitop7.db.entity.Transaction;
import com.semitop7.db.repository.TransactionRepository;
import com.semitop7.dto.model.TransactionsDto;
import com.semitop7.service.db.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    @Transactional
    public List<Transaction> saveAll(TransactionsDto transactions) {
        return transactionRepository.saveAll(transactions.getTransactions());
    }
}
