package com.desafiovotacao.desafiovotacao.domain.repositories;

import com.desafiovotacao.desafiovotacao.domain.entities.Pauta;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PautaRepository extends ReactiveMongoRepository<Pauta, String> {
}