package com.semitop7;

import com.semitop7.db.entity.Client;
import com.semitop7.db.entity.Transaction;
import com.semitop7.dto.model.TransactionsDto;
import org.springframework.scheduling.annotation.AsyncResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public final class ObjectBuilderTest {
    public static List<Future<TransactionsDto>> createFutureTransactions(Long... inn) {
        List<Future<TransactionsDto>> future = new ArrayList<>(2);
        for (int i = 0; i < 2; i++) {
            TransactionsDto t = new TransactionsDto();
            t.setTransactions(createTransactions(inn));
            future.add(new AsyncResult<>(t));
        }
        return future;
    }

    public static List<Transaction> createTransactions(Long... inn) {
        return Arrays.stream(inn)
                .map(ObjectBuilderTest::createOneTransaction)
                .collect(Collectors.toList());
    }

    public static Transaction createOneTransaction(Long inn) {
        Transaction t = new Transaction();
        t.setClient(createOneClient(inn));
        return t;
    }


    public static List<Client> createClients(Long... inn) {
        return Arrays.stream(inn)
                .map(ObjectBuilderTest::createOneClient)
                .collect(Collectors.toList());
    }

    public static Client createOneClient(Long inn) {
        Client c = new Client();
        c.setInn(inn);
        return c;
    }

    public static String testXmlFile() {
        return "<transactions><transaction><place>A PLACE 1</place><amount>10.01</amount><currency>UAH</currency>" +
                "<card>123456****1234</card><client><firstName>Ivan</firstName><lastName>Ivanoff</lastName>" +
                "<middleName>Ivanoff</middleName><inn>1234567890</inn></client></transaction><transaction>" +
                "<place>A PLACE 2</place><amount>9876.01</amount><currency>UAH</currency><card>123456****1234</card>" +
                "<client><firstName>Ivan</firstName><lastName>Ivanoff</lastName><middleName>Ivanoff</middleName>" +
                "<inn>1234567890</inn></client></transaction><transaction><place>A PLACE 3</place><amount>12.01</amount>" +
                "<currency>USD</currency><card>123456****1234</card><client><firstName>Ivan</firstName>" +
                "<lastName>Ivanoff</lastName><middleName>Ivanoff</middleName><inn>1234567890</inn></client></transaction>" +
                "<transaction><place>A PLACE 4</place><amount>12.01</amount><currency>EUR</currency>" +
                "<card>123456****1234</card><client><firstName>Ivan</firstName><lastName>Ivanoff</lastName>" +
                "<middleName>Ivanoff</middleName><inn>1234567890</inn></client></transaction></transactions>";
    }
}
