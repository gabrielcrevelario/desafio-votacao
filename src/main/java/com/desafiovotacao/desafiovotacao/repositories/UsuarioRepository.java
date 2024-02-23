package com.desafiovotacao.desafiovotacao.domain.repositories;

import com.desafiovotacao.desafiovotacao.domain.entities.Usuario;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends ReactiveMongoRepository<Usuario, String> {
}
