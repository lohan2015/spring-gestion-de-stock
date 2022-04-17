package com.kinart.stock.business.repository;

import com.kinart.stock.business.model.Entreprise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntrepriseRepository extends JpaRepository<Entreprise, Integer> {

}
