package com.kinart.stock.business.repository;

import com.kinart.stock.business.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Integer> {

}
