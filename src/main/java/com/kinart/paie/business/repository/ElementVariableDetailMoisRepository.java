package com.kinart.paie.business.repository;

import com.kinart.paie.business.model.ElementVariableDetailMois;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ElementVariableDetailMoisRepository extends JpaRepository<ElementVariableDetailMois, Integer> {

//    @Query(value = "select a, b.nom+' 'b.pren, c.lrub from Salarie b, ElementVariableDetailMois a " +
//            "left join ElementSalaire c on (a.identreprise=c.identreprise and a.rubq=c.crub) "+
//            "where a.identreprise=b.identreprise and " +
//            "a.nmat=b.nmat and a.identreprise=:identreprise and a.aamm=:aamm", nativeQuery = true)
//    List<ElementVariableDetailMois> findAllEVNativeQuery(@Param("aamm") String aamm, Integer identreprise);
//
//    List<ElementVariableDetailMois> findEVByMatriculeNativeQuery(@Param("code") String code);
//
//    List<ElementVariableDetailMois> findEVByIdNativeQuery(@Param("code") String code);
}