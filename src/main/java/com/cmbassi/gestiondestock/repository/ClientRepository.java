package com.cmbassi.gestiondestock.repository;

import com.cmbassi.gestiondestock.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Integer> {

}
