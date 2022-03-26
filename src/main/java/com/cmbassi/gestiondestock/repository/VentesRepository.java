package com.cmbassi.gestiondestock.repository;

import com.cmbassi.gestiondestock.model.Ventes;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentesRepository extends JpaRepository<Ventes, Integer> {

  Optional<Ventes> findVentesByCode(String code);
}
