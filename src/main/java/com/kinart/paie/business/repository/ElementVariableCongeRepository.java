package com.kinart.paie.business.repository;

import com.kinart.paie.business.model.ElementVariableConge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ElementVariableCongeRepository extends JpaRepository<ElementVariableConge, Integer>, JpaSpecificationExecutor<ElementVariableConge> {
}