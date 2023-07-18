package com.kinart.stock.business.repository;

import com.kinart.stock.business.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RolesRepository extends JpaRepository<Roles, Integer> {

   void deleteByUtilisateurId(@Param("id") Integer id);

}
