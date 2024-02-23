package com.desafiovotacao.desafiovotacao.entities;


import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("pauta")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Pauta  {
    @Id
    private String id;
    @NotNull
    private String title;
    private String description;
    private LocalDateTime dateStart;
    private LocalDateTime dateEnd;
    private List<Voto> votos;

    private boolean ativated;
}
