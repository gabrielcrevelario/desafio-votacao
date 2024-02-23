package com.desafiovotacao.desafiovotacao.entities;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("usuario")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Usuario {
    @Id
    private String id;
    @NotNull
    private String name;
    @NotNull
    @CPF
    private String cpf;
    @NotNull
    @Email
    private String email;
    @NotNull
    @Max(20)
    @Min(8)
    private String password;
}
