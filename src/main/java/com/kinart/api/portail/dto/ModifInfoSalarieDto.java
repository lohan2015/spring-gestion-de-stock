package com.mcsoftware.amplituderh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifInfoSalarieDto implements Serializable {

    @NotNull(message = "Le matricule ne doit pas etre vide")
    @NotEmpty(message = "Le matricule ne doit pas etre vide")
    @NotBlank(message = "Le matricule ne doit pas etre vide")
    String matricule;

    @NotNull(message = "L'opération' ne doit pas etre vide")
    @NotEmpty(message = "L'opération ne doit pas etre vide")
    @NotBlank(message = "L'opération ne doit pas etre vide")
    String cdos;

    @NotNull(message = "La clé ne doit pas etre vide")
    @NotEmpty(message = "La clé ne doit pas etre vide")
    @NotBlank(message = "La clé ne doit pas etre vide")
    String key;

    @NotNull(message = "La valeur ne doit pas etre vide")
    @NotEmpty(message = "La valeur ne doit pas etre vide")
    @NotBlank(message = "La valeur ne doit pas etre vide")
    String value;
}
