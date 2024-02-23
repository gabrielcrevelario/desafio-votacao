package com.desafiovotacao.desafiovotacao.repositories;

import com.desafiovotacao.desafiovotacao.entities.Voto;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotoRepository extends ReactiveMongoRepository<Voto, String> {
}