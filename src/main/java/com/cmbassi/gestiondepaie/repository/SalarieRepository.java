package com.cmbassi.gestiondepaie.repository;

import com.cmbassi.gestiondepaie.model.Salarie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SalarieRepository extends JpaRepository<Salarie, Integer> {

    @Query("select a from Salarie a where cals='O' and nmat like :nmat")
   List<Salarie> findByMatricule(@Param("nmat") String nmat);

   @Query("select a from Salarie a where nmat = :nmat")
   Salarie findByMatriculeExactly(@Param("nmat") String nmat);

    @Query("select a from Salarie a where (upper(nom) like upper(:nom) or upper(pren) like upper(:nom))")
    List<Salarie> findByName(@Param("nom") String nom);

    @Query("select a from Salarie a where cals='N' and nmat like :nmat")
    List<Salarie> findByMatriculeInactif(@Param("nmat") String nmat);

    // Caisses et mutuelles
    @Transactional
    @Modifying
    @Query("delete from CaisseMutuelleSalarie where nmat = :nmat")
    void deleteCaisseMutuelleByMatricule(@Param("nmat") String nmat);

    @Transactional
    @Modifying
    @Query("delete from CaisseMutuelleSalarie where idsalairie = :idsalairie")
    void deleteCaisseMutuelleByIdSalarie(@Param("idsalairie") Integer idsalairie);

    // Eléments fixes
    @Transactional
    @Modifying
    @Query("delete from ElementFixeSalaire where nmat = :nmat")
    void deleteElementFixeByMatricule(@Param("nmat") String nmat);

    @Transactional
    @Modifying
    @Query("delete from ElementFixeSalaire where idsalairie = :idsalairie")
    void deleteElementFixeByIdSalarie(@Param("idsalairie") Integer idsalairie);

    // Prêts internes
    @Transactional
    @Modifying
    @Query("delete from PretInterne where nmat = :nmat")
    void deletePretInterneByMatricule(@Param("nmat") String nmat);

    @Transactional
    @Modifying
    @Query("delete from PretInterne where idsalairie = :idsalairie")
    void deletePretInterneByIdSalarie(@Param("idsalairie") Integer idsalairie);

    // Banques
    @Transactional
    @Modifying
    @Query("delete from VirementSalarie where nmat = :nmat")
    void deleteBanqueByMatricule(@Param("nmat") String nmat);


    @Transactional
    @Modifying
    @Query("delete from VirementSalarie where idsalairie = :idsalairie")
    void deleteBanqueByIdSalarie(@Param("idsalairie") Integer idsalairie);
}