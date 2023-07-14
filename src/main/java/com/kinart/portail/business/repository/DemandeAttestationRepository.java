package com.kinart.portail.business.repository;

import com.kinart.portail.business.model.DemandeAbsenceConge;
import com.kinart.portail.business.model.DemandeAttestation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author c.mbassi
 * 06.07.2023
 */
public interface DemandeAttestationRepository extends JpaRepository<DemandeAttestation, Integer> {

   List<DemandeAttestation> findByScePersonnel(@Param("scePersonnel") String scePersonnel);

    @Query("from DemandeAttestation where scePersonnel=:emailadr and status=:status ORDER BY creationDate DESC")  // JPQL
    List<DemandeAttestation> searchByEmailAndStatus(@Param("emailadr") String email, @Param("status") String status);

    @Query(value = "select * from demandeattestation d " +
                    "inner join utilisateur u on d.user_id = u.id and d.identreprise = u.identreprise "+
                    "where u.email=:email ORDER BY d.creation_date DESC", nativeQuery = true)  // SQL natif
    List<DemandeAbsenceConge> searchByUserEmail(@Param("email") String email);

   @Query(value = "select * from demandeattestation where user_id=:userID and typedoc=:typeDoc ORDER BY creationDate DESC", nativeQuery = true)  // SQL natif
   List<DemandeAttestation> searchByUserIDAndTypeDoc(@Param("userID") Integer userID, @Param("typeDoc") String typeDoc);

    @Query(value = "select * from demandeattestation d "+
            "where d.scepersonnel=:email and d.creation_date between :start and :end and d.status=:status "+
            "ORDER BY d.creation_date DESC", nativeQuery = true)  // SQL natif
    List<DemandeAttestation> searchByUserEmailAndPeriodeStatus(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("email") String email, @Param("status") String status);

}
