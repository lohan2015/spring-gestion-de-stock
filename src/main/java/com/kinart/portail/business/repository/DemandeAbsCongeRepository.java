package com.kinart.portail.business.repository;

import com.kinart.portail.business.model.DemandeAbsenceConge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author c.mbassi
 * 06.07.2023
 */
public interface DemandeAbsCongeRepository extends JpaRepository<DemandeAbsenceConge, Integer> {

   List<DemandeAbsenceConge> findByValid1(@Param("valid1") String valid1);

    List<DemandeAbsenceConge> findByValid2(@Param("valid2") String valid2);

    List<DemandeAbsenceConge> findByValid3(@Param("valid3") String valid3);

    List<DemandeAbsenceConge> findByValid4(@Param("valid4") String valid4);

    @Query("from DemandeAbsenceConge where valid1=:emailadr or valid2=:emailadr or valid3=:emailadr or valid4=:emailadr ORDER BY creationDate DESC")  // JPQL
    List<DemandeAbsenceConge> searchByEmail(@Param("emailadr") String email);

    @Query("from DemandeAbsenceConge where (valid1=:emailadr and status1=:status) or (valid2=:emailadr and status2=:status) or (valid3=:emailadr and status3=:status) or (valid4=:emailadr and status4=:status) ORDER BY creationDate DESC")  // JPQL
    List<DemandeAbsenceConge> searchByEmailAndStatus(@Param("emailadr") String email, @Param("status") String status);

    @Query(value = "select * from demandeabsenceconge d "+
                   "inner join utilisateur u on d.user_id = u.id and d.identreprise = u.identreprise "+
                   "where u.email=:email ORDER BY d.creation_date DESC", nativeQuery = true)  // SQL natif
    List<DemandeAbsenceConge> searchByUserEmail(@Param("email") String email);
}
