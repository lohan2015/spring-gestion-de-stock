package com.kinart.portail.business.repository;

import com.kinart.portail.business.model.DemandeAbsenceConge;
import com.kinart.portail.business.model.DemandeAttestation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author c.mbassi
 * 06.07.2023
 */
public interface DemandeAttestationRepository extends JpaRepository<DemandeAttestation, Integer> {

   List<DemandeAttestation> findByScePersonnel(@Param("scePersonnel") String scePersonnel);

    @Query("from DemandeAttestation where scePersonnel=:emailadr")  // JPQL
    List<DemandeAttestation> searchByEmail(@Param("emailadr") String email);

    @Query("from DemandeAttestation where scePersonnel=:emailadr and status=:status ORDER BY creationDate DESC")  // JPQL
    List<DemandeAttestation> searchByEmailAndStatus(@Param("emailadr") String email, @Param("status") String status);

    @Query(value = "select * from demandeattestation d inner join utilisateur u where d.user_id = u.id and u.email=:email ORDER BY d.creationDate DESC", nativeQuery = true)  // SQL natif
    List<DemandeAbsenceConge> searchByUserEmail(@Param("email") String email);

   @Query(value = "select * from demandeattestation where user_id=:userID and typedoc=:typeDoc ORDER BY creationDate DESC", nativeQuery = true)  // SQL natif
   List<DemandeAttestation> searchByUserIDAndTypeDoc(@Param("userID") Integer userID, @Param("typeDoc") String typeDoc);
}
