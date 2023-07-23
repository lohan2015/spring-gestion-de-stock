package com.kinart.portail.business.repository;

import com.kinart.api.portail.dto.DemandeHabilitationResponse;
import com.kinart.api.portail.dto.DemandeModifInfoResponse;
import com.kinart.portail.business.model.DemandeAbsenceConge;
import com.kinart.portail.business.model.DemandeHabilitation;
import com.kinart.portail.business.model.DemandeModifInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author c.mbassi
 * 06.07.2023
 */
public interface DemandeModifInfoRepository extends JpaRepository<DemandeModifInfo, Integer> {

   List<DemandeModifInfo> findByValid(@Param("valid") String valid);

    @Query("from DemandeModifInfo where valid=:emailadr")  // JPQL
    List<DemandeModifInfo> searchByEmail(@Param("emailadr") String email);

    @Query("from DemandeModifInfo where valid=:emailadr and status=:status ORDER BY creationDate DESC")  // JPQL
    List<DemandeModifInfo> searchByEmailAndStatus(@Param("emailadr") String email, @Param("status") String status);

    @Query(value = "select * from demandemodifinfo d " +
                   "inner join utilisateur u on d.user_id = u.id and d.identreprise = u.identreprise "+
                   "where u.email=:email ORDER BY d.creation_date DESC", nativeQuery = true)  // SQL natif
    List<DemandeModifInfo> searchByUserEmail(@Param("email") String email);

    @Query(value = "select * from demandemodifinfo d "+
            "where d.valid=:email and d.creation_date between :start and :end and d.status=:status "+
            "ORDER BY d.creation_date DESC", nativeQuery = true)  // SQL natif
    List<DemandeModifInfo> searchByUserEmailAndPeriodeStatus(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("email") String email, @Param("status") String status);

    @Modifying
    @Query(value = "update demandemodifinfo set status = :status where id = :id", nativeQuery = true)
    void updateDemande(@Param(value = "id") Integer id, @Param(value = "status") String status);

    @Modifying
    @Query(value = "delete demandemodifinfo where id = :id", nativeQuery = true)
    void deleteDemande(@Param(value = "id") Integer id);

    @Query(value = "select new com.kinart.api.portail.dto.DemandeModifInfoResponse(im.id, im.creationDate, im.idEntreprise, im.valid, im.userDemModInfo) from com.kinart.portail.business.model.DemandeModifInfo im where im.id=:id", nativeQuery = false)
    DemandeModifInfoResponse searchDemandeById(@Param(value = "id") Integer id);

}
