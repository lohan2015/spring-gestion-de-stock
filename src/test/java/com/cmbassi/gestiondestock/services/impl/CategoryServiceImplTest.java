package com.cmbassi.gestiondestock.services.impl;

import com.cmbassi.gestiondestock.dto.CategoryDto;
import com.cmbassi.gestiondestock.exception.EntityNotFoundException;
import com.cmbassi.gestiondestock.exception.ErrorCodes;
import com.cmbassi.gestiondestock.exception.InvalidEntityException;
import com.cmbassi.gestiondestock.services.CategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class CategoryServiceImplTest {

    @Autowired
    private CategoryService service;

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