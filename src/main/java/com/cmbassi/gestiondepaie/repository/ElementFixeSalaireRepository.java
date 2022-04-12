package com.cmbassi.gestiondepaie.repository;

import com.cmbassi.gestiondepaie.model.ElementFixeSalaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ElementFixeSalaireRepository extends JpaRepository<ElementFixeSalaire, Integer>, JpaSpecificationExecutor<ElementFixeSalaire> {
}