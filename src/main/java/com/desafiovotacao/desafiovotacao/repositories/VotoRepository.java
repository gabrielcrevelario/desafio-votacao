package com.desafiovotacao.desafiovotacao.domain.repositories;

import com.desafiovotacao.desafiovotacao.domain.entities.Voto;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotoRepository extends ReactiveMongoRepository<Voto, String> {
}