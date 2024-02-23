package com.desafiovotacao.desafiovotacao.services;

import com.desafiovotacao.desafiovotacao.entities.Pauta;
import com.desafiovotacao.desafiovotacao.entities.Usuario;
import com.desafiovotacao.desafiovotacao.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.ZonedDateTime;

@Service
public class UsuarioService {
    private UsuarioRepository usuarioRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;
    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository,ReactiveMongoTemplate reactiveMongoTemplate) {
        this.usuarioRepository = usuarioRepository;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }
    public Mono<Usuario> CreateOrUpdate(Usuario usuario) {
        return reactiveMongoTemplate.exists(Query.query(Criteria.where("cpf").is(usuario.getCpf())), Usuario.class).flatMap(u -> {
             if(u) return Mono.error(new Exception("Usuario já existe"));
             else return usuarioRepository.save(usuario);
        });

    }
    public Flux<Usuario> findAll() {
        return usuarioRepository.findAll();
    }
    public Mono<Usuario> findById(String id) {
        return usuarioRepository.findById(id);
    }
    public Mono<Usuario> findByCpf(String cpf) {
        return reactiveMongoTemplate.findOne(Query.query(Criteria.where("cpf").is(cpf)), Usuario.class);
    }
}
