package com.desafiovotacao.desafiovotacao.presentation.controller;

import com.desafiovotacao.desafiovotacao.domain.entities.Pauta;
import com.desafiovotacao.desafiovotacao.domain.entities.Teste;
import com.desafiovotacao.desafiovotacao.domain.repositories.TesteRepository;
import com.desafiovotacao.desafiovotacao.services.PautaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/pautas")
public class PautaController {
    @Autowired
    private PautaService pautaService;
    @Autowired
    private TesteRepository testeRepository;
    @GetMapping
    public Flux<Pauta> findAllPautas() {
        return pautaService.findAllPautas();
    }
    @GetMapping("/teste")
    public  Flux<ResponseEntity<Teste>> getTeste() {
        return testeRepository.findAll().map(t -> ResponseEntity.ok(t));
    }
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Pauta>> getPautaById(@PathVariable String id) {
       return pautaService.findPautabyId(id).map(p -> ResponseEntity.ok(p))
               .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Pauta> createPauta(@RequestBody Pauta pauta) {
        return pautaService.create(pauta);
    }
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Pauta>> updatePauta(@PathVariable("id") String id, @RequestBody Pauta pauta) {
        return pautaService.update(id, pauta)
                .map(updatedPauta -> ResponseEntity.ok(updatedPauta))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @Scheduled(fixedRate = 1000)
    public Flux<Pauta> verifyPautas() {
       return pautaService.verifyPautasIsActive();
    }
}
