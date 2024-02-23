package com.desafiovotacao.desafiovotacao.services;

import com.desafiovotacao.desafiovotacao.entities.Pauta;
import com.desafiovotacao.desafiovotacao.repositories.PautaRepository;
import com.desafiovotacao.desafiovotacao.repositories.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Service
public class PautaService {
    private final PautaRepository pautaRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;
    @Autowired
    public PautaService(PautaRepository pautaRepository,
                        ReactiveMongoTemplate reactiveMongoTemplate) {
        this.pautaRepository = pautaRepository;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }
    public Flux<Pauta> findAllPautas() {
       return this.pautaRepository.findAll();
    }
    public Mono<Pauta> findPautabyId(String id) {
        return this.pautaRepository.findById(id);
    }
    public Mono<Pauta> create(Pauta pauta) {
        try {
            var now = LocalDateTime.now();
            pauta.setDateStart(now);
            pauta.setDateEnd(now.plusMinutes(1));
            pauta.setAtivated(false);
            return this.pautaRepository.insert(pauta);
        } catch (Exception ex) {
            throw ex;
        }
    }
    public Mono<Pauta> update(String pautaId, Pauta pauta) {
        try {
            pauta.setId(pautaId);
           return pautaRepository.existsById(pautaId).flatMap(f -> {
                if(f) return this.pautaRepository.save(pauta);
                return Mono.error(new Exception("pauta não existe"));
            });


        } catch (Exception ex) {
            throw ex;
        }
    }
    public Flux<Pauta> verifyPautasIsActive() {
        ZonedDateTime currentTime = ZonedDateTime.now();
        ZonedDateTime oneMinuteAgo = currentTime.minusMinutes(1); // Um minuto atrás

        return reactiveMongoTemplate.find(Query.query(Criteria.where("ativo").is(true)), Pauta.class)
                .flatMap(pauta -> {
                    LocalDateTime dataInicio = pauta.getDateStart();
                    Duration duration = Duration.between(dataInicio, currentTime);
                    if (duration.compareTo(Duration.ofMinutes(1)) > 0) {
                        pauta.setAtivated(false); // Define ativo como false
                        return reactiveMongoTemplate.save(pauta);
                    }
                    return Mono.empty(); // Não faz nada se a pauta está ativa há menos de um minuto
                });
    }
}
