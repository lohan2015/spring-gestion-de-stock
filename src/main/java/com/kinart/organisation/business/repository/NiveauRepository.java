package com.kinart.organisation.business.repository;

import com.kinart.organisation.business.model.Orgniveau;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NiveauRepository extends JpaRepository<Orgniveau, Integer> {
}
