package com.kinart.organisation.business.repository;

import com.kinart.organisation.business.model.Orgniveauemploitype;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NiveauOrganigrammeRepository  extends JpaRepository<Orgniveauemploitype, Integer> {
}
