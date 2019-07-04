package com.semitop7.service.db.impl;

import com.semitop7.db.entity.Transaction;
import com.semitop7.db.repository.TransactionRepository;
import com.semitop7.dto.model.TransactionsDto;
import com.semitop7.service.db.TransactionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.semitop7.ObjectBuilderTest.createTransactions;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionServiceImplTest {
    @Autowired
    private TransactionService transactionService;

    @MockBean
    private TransactionRepository transactionRepository;

    @Test
    public void testAssert() {
        assertThat(transactionService).isNotNull();
    }

    @Test
    public void saveAll() {
        List<Transaction> list = createTransactions(123456789L, 123456789L, 987654321L);
        TransactionsDto dto = new TransactionsDto();
        dto.setTransactions(list);

        transactionService.saveAll(dto);
        transactionService.saveAll(list);

        Mockito.verify(transactionRepository,
                Mockito.times(2)).saveAll(list);
    }
}