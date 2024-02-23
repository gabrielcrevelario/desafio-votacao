package com.desafiovotacao.desafiovotacao.controller;

import com.desafiovotacao.desafiovotacao.entities.Pauta;
import com.desafiovotacao.desafiovotacao.entities.Usuario;
import com.desafiovotacao.desafiovotacao.entities.VotoUser;
import com.desafiovotacao.desafiovotacao.repositories.TesteRepository;
import com.desafiovotacao.desafiovotacao.services.PautaService;
import com.desafiovotacao.desafiovotacao.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;
    @GetMapping
    public Flux<Usuario> findAllUsuarios() {
        return usuarioService.findAll();
    }

    @GetMapping("/usuarioId/{id}")
    public Mono<ResponseEntity<Usuario>> getUsuarioById(@PathVariable String id) {
       return usuarioService.findById(id).map(ResponseEntity::ok)
               .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Usuario> createUsuario(@RequestBody Usuario usuario) {
        return usuarioService.CreateOrUpdate(usuario);
    }
    @GetMapping("/cpf/{cpf}")
    public Mono<ResponseEntity<Usuario>> findByCpf(@PathVariable("cpf") String cpf) {
        return usuarioService.findByCpf(cpf)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
