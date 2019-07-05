package com.semitop7.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.semitop7.db.entity.Transaction;

import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "transactions")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionsDto {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "transaction")
    private List<Transaction> transactions;

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transaction) {
        this.transactions = transaction;
    }

    public void addTransaction(Transaction transaction) {
        if (transactions == null) {
            transactions = new ArrayList<>();
        }
        transactions.add(transaction);
    }
}
