package com.cmbassi.gestiondestock.services.impl;

import com.cmbassi.gestiondepaie.model.ElementVariableDetailMois;
import com.cmbassi.gestiondepaie.model.ParamData;
import com.cmbassi.gestiondepaie.model.Salarie;
import com.cmbassi.gestiondepaie.services.utils.GeneriqueConnexionService;
import com.cmbassi.gestiondestock.dto.CategoryDto;
import com.cmbassi.gestiondestock.exception.EntityNotFoundException;
import com.cmbassi.gestiondestock.exception.ErrorCodes;
import com.cmbassi.gestiondestock.exception.InvalidEntityException;
import com.cmbassi.gestiondestock.services.CategoryService;
import org.hibernate.Session;
import org.hibernate.type.StandardBasicTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.Query;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class CategoryServiceImplTest {

    @Autowired
    private CategoryService service;

    @Autowired
    private GeneriqueConnexionService generiqueConnexionService;

    @Test
    void shoulSaveParamDataWithSuccess() {
        ParamData expectedParam = ParamData.builder()
                .ctab(Integer.valueOf(1))
                .cacc("001")
                .vall("Direction")
                .valm(new Long(1))
                .duti("AUTO")
                .nume(1)
                .idEntreprise(1)
                .build();

        ElementVariableDetailMois expectedEV = ElementVariableDetailMois.builder()
                .aamm("202203")
                .rubq("0001")
                .nbul(9)
                .nmat("000005")
                .cuti("AUTO")
                .idEntreprise(1)
                .build();

        Salarie expectedSalarie = Salarie.builder()
                .codesite("202203")
                .cals("O")
                .cat("04")
                .nmat("000005")
                .nom("MBASSI MASSOUKE")
                .pren("CYRILLE")
                .lemb("AUTO")
                .idEntreprise(1)
                .build();

        Session session = generiqueConnexionService.getSession();
        //Transaction tx = session.beginTransaction();

        session.save(expectedSalarie);
        session.save(expectedParam);
        session.save(expectedEV);

       // tx.commit();
       // generiqueConnexionService.closeSession(session);

        // Lecture des données
        session = generiqueConnexionService.getSession();
        String query = "SELECT e.*, s.nom as nomsal, s.pren as prensal FROM ElementVariableDetailMois e " +
                "LEFT JOIN Salarie s ON (e.identreprise=s.identreprise AND e.nmat=s.nmat) "+
                "WHERE e.identreprise=:identreprise";
        Query q = session.createSQLQuery(query).addEntity("e", ElementVariableDetailMois.class).addScalar("nomsal", StandardBasicTypes.STRING).addScalar("prensal", StandardBasicTypes.STRING);
        q.setParameter("identreprise", 1);

        List<Object[]> lst = q.getResultList();
        generiqueConnexionService.closeSession(session);

        for (Object[] o : lst)
        {
            Assertions.assertNotNull(o[0]);
            Assertions.assertEquals("MBASSI MASSOUKE", o[1].toString());
            Assertions.assertEquals("CYRILLE", o[2].toString());
        }
    }

    @Test
    void shoulSaveCategoryWithSuccess() {
        CategoryDto expectedCategory = CategoryDto.builder()
                .code("Cat test")
                .designation("Designation test")
                .idEntreprise(1)
                .build();
        CategoryDto savedCategory = service.save(expectedCategory);
        Assertions.assertNotNull(savedCategory);
        Assertions.assertNotNull(savedCategory.getId());
        Assertions.assertEquals(expectedCategory.getCode(), savedCategory.getCode());
        Assertions.assertEquals(expectedCategory.getDesignation(), savedCategory.getDesignation());
        Assertions.assertEquals(expectedCategory.getIdEntreprise(), savedCategory.getIdEntreprise());
    }

    @Test
    void shoulIdThrowsInvalidEntityException() {
        CategoryDto expectedCategory = CategoryDto.builder().build();
        InvalidEntityException expectedException = assertThrows(InvalidEntityException.class, () -> service.save(expectedCategory));

        assertEquals(ErrorCodes.CATEGORY_NOT_VALID, expectedException.getErrorCode());
        assertEquals(1, expectedException.getErrors().size());
        assertEquals("Veuillez renseigner le code de la categorie", expectedException.getErrors().get(0));
    }

    @Test
    void shoulIdThrowsEntityNotFoundException() {

        EntityNotFoundException expectedException = assertThrows(EntityNotFoundException.class, () -> service.findById(0));

        assertEquals(ErrorCodes.CATEGORY_NOT_FOUND, expectedException.getErrorCode());
        assertEquals("Aucune category avec l'ID = 0 n' ete trouve dans la BDD", expectedException.getMessage());
    }

    /*@Test(expected = EntityNotFoundException.class)// Use in JUNIT 4
    void shoulIdThrowsEntityNotFoundExceptionTwo() {
        service.findById(0);
    }*/

}