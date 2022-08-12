package com.kinart.organisation.business.repository;

import com.kinart.organisation.business.model.Organigramme;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganigrammeRepository extends JpaRepository<Organigramme, Integer> {
}
