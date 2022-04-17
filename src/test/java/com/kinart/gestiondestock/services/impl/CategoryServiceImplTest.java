package com.kinart.gestiondestock.services.impl;

import com.kinart.paie.business.model.ElementVariableDetailMois;
import com.kinart.paie.business.model.ParamData;
import com.kinart.paie.business.model.Salarie;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.paie.business.services.utils.HibernateConnexionService;
import com.kinart.api.gestiondestock.dto.CategoryDto;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.ErrorCodes;
import com.kinart.stock.business.exception.InvalidEntityException;
import com.kinart.stock.business.services.CategoryService;
import org.hibernate.Session;
import org.hibernate.type.StandardBasicTypes;
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

    @Autowired
    private HibernateConnexionService hibernateConnexionService;

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

        Session session = hibernateConnexionService.getSession();
        //Transaction tx = session.beginTransaction();

        session.save(expectedSalarie);
        session.save(expectedParam);
        session.save(expectedEV);

       // tx.commit();
       // generiqueConnexionService.closeSession(session);

        // Lecture des données
        session = hibernateConnexionService.getSession();
        String query = "SELECT e.*, s.nom as nomsal, s.pren as prensal FROM ElementVariableDetailMois e " +
                "LEFT JOIN Salarie s ON (e.identreprise=s.identreprise AND e.nmat=s.nmat) "+
                "WHERE e.identreprise=:identreprise";
        Query q = session.createSQLQuery(query).addEntity("e", ElementVariableDetailMois.class).addScalar("nomsal", StandardBasicTypes.STRING).addScalar("prensal", StandardBasicTypes.STRING);
        q.setParameter("identreprise", 1);

        List<Object[]> lst = q.getResultList();
        hibernateConnexionService.closeConnexion(session);

        for (Object[] o : lst)
        {
//            Assertions.assertNotNull(o[0]);
//            Assertions.assertEquals("MBASSI MASSOUKE", o[1].toString());
//            Assertions.assertEquals("CYRILLE", o[2].toString());
        }

        List result = hibernateConnexionService.find("From Salarie WHERE nmat='000005'");
        if(result.size()>0){
            for (Object[] o : lst)
            {
//                Assertions.assertNotNull(o[0]);
//                Assertions.assertEquals("MBASSI MASSOUKE", o[1].toString());
//                Assertions.assertEquals("CYRILLE", o[2].toString());
            }
        }
//
//        result = generiqueConnexionService.find("From Salarie WHERE nmat='000005'");
//        if(result.size()>0){
//            for (Object[] o : lst)
//            {
//                Assertions.assertNotNull(o[0]);
//                Assertions.assertEquals("MBASSI MASSOUKE", o[1].toString());
//                Assertions.assertEquals("CYRILLE", o[2].toString());
//            }
//        }
    }

    @Test
    void shoulSaveCategoryWithSuccess() {
        CategoryDto expectedCategory = CategoryDto.builder()
                .code("Cat test")
                .designation("Designation test")
                .idEntreprise(1)
                .build();

        Session session = hibernateConnexionService.getSession();
        session.save(CategoryDto.toEntity(expectedCategory));
        //session.flush();
        hibernateConnexionService.closeConnexion(session);
//        Assertions.assertNotNull(savedCategory);
//        Assertions.assertNotNull(savedCategory.getId());
//        Assertions.assertEquals(expectedCategory.getCode(), savedCategory.getCode());
//        Assertions.assertEquals(expectedCategory.getDesignation(), savedCategory.getDesignation());
//        Assertions.assertEquals(expectedCategory.getIdEntreprise(), savedCategory.getIdEntreprise());
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
        //assertEquals("Aucune category avec l'ID = 0 n' ete trouve dans la BDD", expectedException.getMessage());
    }

    /*@Test(expected = EntityNotFoundException.class)// Use in JUNIT 4
    void shoulIdThrowsEntityNotFoundExceptionTwo() {
        service.findById(0);
    }*/

}