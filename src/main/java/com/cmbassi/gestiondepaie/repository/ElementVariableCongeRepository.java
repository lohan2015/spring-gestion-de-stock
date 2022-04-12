package com.cmbassi.gestiondepaie.repository;

import com.cmbassi.gestiondepaie.model.ElementVariableConge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ElementVariableCongeRepository extends JpaRepository<ElementVariableConge, Integer>, JpaSpecificationExecutor<ElementVariableConge> {
}