package com.semitop7.db.repository;

import com.semitop7.db.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findClientByInn(Integer inn);
}