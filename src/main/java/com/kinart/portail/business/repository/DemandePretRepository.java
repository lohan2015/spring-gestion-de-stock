package com.kinart.portail.business.repository;

import com.kinart.portail.business.model.DemandeAbsenceConge;
import com.kinart.portail.business.model.DemandePret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author c.mbassi
 * 06.07.2023
 */
public interface DemandePretRepository extends JpaRepository<DemandePret, Integer> {

   List<DemandePret> findByScePersonnel(@Param("scePersonnel") String scePersonnel);

    List<DemandePret> findByDrhl(@Param("drhl") String drhl);

    List<DemandePret> findByDga(@Param("dga") String dga);

    List<DemandePret> findByDg(@Param("dg") String dg);

    @Query("from DemandePret where scePersonnel=:emailadr or drhl=:emailadr or dga=:emailadr or dg=:emailadr ORDER BY creationDate DESC")  // JPQL
    List<DemandePret> searchByEmail(@Param("emailadr") String email);

    @Query("from DemandePret where (scePersonnel=:emailadr and status1=:status) or (drhl=:emailadr and status2=:status) or (dga=:emailadr and status3=:status) or (dg=:emailadr and status4=:status) ORDER BY creationDate DESC")  // JPQL
    List<DemandePret> searchByEmailAndStatus(@Param("emailadr") String email, @Param("status") String status);

    @Query(value = "select * from demandepret d inner join utilisateur u where d.user_id = u.id and u.email=:email ORDER BY d.creationDate DESC", nativeQuery = true)  // SQL natif
    List<DemandeAbsenceConge> searchByUserEmail(@Param("email") String email);

   @Query(value = "select * from demandepret where user_id=:userID and typepret=:typePret ORDER BY creationDate DESC", nativeQuery = true)  // SQL natif
   List<DemandePret> searchByUserIDAndTypePret(@Param("userID") Integer userID, @Param("typePret") String typePret);
}
