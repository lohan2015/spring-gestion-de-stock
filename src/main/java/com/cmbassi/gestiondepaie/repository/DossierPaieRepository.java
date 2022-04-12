package com.cmbassi.gestiondepaie.repository;

import com.cmbassi.gestiondepaie.model.DossierPaie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DossierPaieRepository extends JpaRepository<DossierPaie, Integer> {
}