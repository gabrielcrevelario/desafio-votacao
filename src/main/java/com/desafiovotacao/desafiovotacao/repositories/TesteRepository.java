package com.desafiovotacao.desafiovotacao.domain.repositories;

import com.desafiovotacao.desafiovotacao.domain.entities.Teste;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TesteRepository extends ReactiveMongoRepository<Teste, String> {
}
