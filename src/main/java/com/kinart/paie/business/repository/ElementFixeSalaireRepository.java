package com.kinart.paie.business.repository;

import com.kinart.paie.business.model.ElementFixeSalaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ElementFixeSalaireRepository extends JpaRepository<ElementFixeSalaire, Integer>, JpaSpecificationExecutor<ElementFixeSalaire> {
}