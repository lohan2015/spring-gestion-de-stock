package com.kinart.paie.business.repository;

import com.kinart.paie.business.model.ElementSalaireBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ElementSalaireBaseRepository extends JpaRepository<ElementSalaireBase, Integer>, JpaSpecificationExecutor<ElementSalaireBase> {
}