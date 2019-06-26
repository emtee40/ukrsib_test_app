package com.semitop7.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.semitop7.enums.Currency;
import com.semitop7.resolver.ClientDeserializer;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.CascadeType.*;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "s_transaction")
public class Transaction implements Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "place")
    @JacksonXmlProperty(localName = "place")
    private String place;

    @Column(name = "amount")
    @JacksonXmlProperty(localName = "amount")
    private float amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    @JacksonXmlProperty(localName = "currency")
    private Currency currency;

    @Column(name = "card")
    @JacksonXmlProperty(localName = "card")
    private String card;

    @ManyToOne(fetch = FetchType.LAZY)
    @JacksonXmlProperty(localName = "client")
    @JoinColumn(name = "client_inn")
    @JsonDeserialize(using = ClientDeserializer.class)
    private Client client;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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
}
