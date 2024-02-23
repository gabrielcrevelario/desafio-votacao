package com.desafiovotacao.desafiovotacao.controller;

import com.desafiovotacao.desafiovotacao.entities.Pauta;
import com.desafiovotacao.desafiovotacao.entities.Voto;
import com.desafiovotacao.desafiovotacao.entities.VotoUser;
import com.desafiovotacao.desafiovotacao.services.PautaService;
import com.desafiovotacao.desafiovotacao.services.VotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/votos")
public class VotoController {
    @Autowired
    private VotoService votoService;
    @PostMapping("/pautaId/{pautaId}/cpf/{cpf}")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Pauta> createVoto(@PathVariable  String pautaId,@PathVariable String cpf, @RequestBody VotoUser voto) throws Exception {
        return votoService.create(pautaId,cpf, voto.getVotoUser());
    }
    @GetMapping("/id/{pautaId}")
    @ResponseStatus(HttpStatus.OK)
    public Flux<ResponseEntity<Voto>> findVotosById(@PathVariable  String pautaId) {
        return votoService.findAllByPautaId(pautaId).map(p -> ResponseEntity.ok(p))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
