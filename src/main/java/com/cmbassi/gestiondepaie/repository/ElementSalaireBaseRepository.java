package com.cmbassi.gestiondepaie.repository;

import com.cmbassi.gestiondepaie.model.ElementSalaireBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ElementSalaireBaseRepository extends JpaRepository<ElementSalaireBase, Integer>, JpaSpecificationExecutor<ElementSalaireBase> {
}