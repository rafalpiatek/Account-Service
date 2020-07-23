package com.piatek.rafal.account_service.repository;

import com.piatek.rafal.account_service.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, String> {

}
