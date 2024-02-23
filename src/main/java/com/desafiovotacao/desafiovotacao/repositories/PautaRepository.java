package com.desafiovotacao.desafiovotacao.repositories;

import com.desafiovotacao.desafiovotacao.entities.Pauta;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PautaRepository extends ReactiveMongoRepository<Pauta, String> {
}