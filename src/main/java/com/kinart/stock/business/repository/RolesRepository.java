package com.kinart.stock.business.repository;

import com.kinart.stock.business.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RolesRepository extends JpaRepository<Roles, Integer> {

   @Modifying
   @Query(value = "DELETE FROM Roles "+
           "where idutilisateur=:id", nativeQuery = true)  // SQL natif
   void deleteRoleByUtilisateurId(@Param("id") Integer id);

   Roles findByUtilisateur_Id(@Param("id") Integer id);

}
