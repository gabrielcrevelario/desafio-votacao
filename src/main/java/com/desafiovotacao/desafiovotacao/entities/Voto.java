package com.desafiovotacao.desafiovotacao.entities;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Document("voto")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Voto {
    @Id
    private String id;
    @NotNull
    private boolean voto;
    @NotNull
    private LocalDateTime dateStart;
    @NotNull
    private String pautaId;
    private String userId;
    @NotNull
    private Usuario usuario;

}
