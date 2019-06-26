package com.semitop7.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.CascadeType.*;

@Entity
@Table(name = "s_client")
@JacksonXmlRootElement(localName = "client")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Client {
    @Id
    @Column(name = "inn", unique = true, nullable = false)
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long inn;

    @Column(name = "first_name")
    @JacksonXmlProperty(localName = "firstName")
    private String firstName;

    @Column(name = "last_name")
    @JacksonXmlProperty(localName = "lastName")
    private String lastName;

    @Column(name = "middle_name")
    @JacksonXmlProperty(localName = "middleName")
    private String middleName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_created", updatable = false)
    @CreationTimestamp
    @JsonIgnore
    private Date dateCreated;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_updated")
    @UpdateTimestamp
    @JsonIgnore
    private Date dateUpdated;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = {PERSIST, REFRESH, MERGE})
    private Set<Transaction> transactions;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void addTransaction(Transaction transaction) {
        if (transactions == null) {
            transactions = new HashSet<>();
        }
        transactions.add(transaction);
    }

    public Long getInn() {
        return inn;
    }

    public void setInn(Long inn) {
        this.inn = inn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(inn, client.inn) &&
                Objects.equals(firstName, client.firstName) &&
                Objects.equals(lastName, client.lastName) &&
                Objects.equals(middleName, client.middleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inn, firstName, lastName, middleName);
    }
}