package com.desafiovotacao.desafiovotacao.repositories;

import com.desafiovotacao.desafiovotacao.entities.Pauta;
import com.desafiovotacao.desafiovotacao.entities.Usuario;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UsuarioRepository extends ReactiveMongoRepository<Usuario, String> {
    Mono<Usuario> findByCpf(String cpf);
}
