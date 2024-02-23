package com.desafiovotacao.desafiovotacao.services;

import com.desafiovotacao.desafiovotacao.entities.Pauta;
import com.desafiovotacao.desafiovotacao.entities.Usuario;
import com.desafiovotacao.desafiovotacao.entities.Voto;
import com.desafiovotacao.desafiovotacao.repositories.PautaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class pautaServiceTest {
    @InjectMocks
    private PautaService pautaService;
    PautaRepository pautaRepository = mock(PautaRepository.class);
    @Mock
    private ReactiveMongoTemplate reactiveMongoTemplate;


    @Test
    public void testCreate() {
        // Crie uma pauta de exemplo
        Pauta pauta = new Pauta();
        pauta.setId("fd31bca1ef884b4aa43d7cb053ccbf3a");
        pauta.setTitle("Pauta 1");
        pauta.setDescription("Descrição da pauta 1");
        pauta.setDateStart(LocalDateTime.now());
        pauta.setDateEnd(LocalDateTime.now().plusMinutes(1)); // Pauta ativa por 1 minuto

        // Mock o comportamento do ReactiveMongoTemplate
        when(pautaRepository.insert(pauta)).thenReturn(Mono.just(pauta));
        // Chame o método de criação da pauta
        Mono<Pauta> pautaMono = pautaService.create(pauta);

        // Verifique se a pauta foi criada corretamente
        StepVerifier.create(pautaMono)
                .expectNext(pauta) // Verifique se a pauta retornada é igual à pauta de exemplo
                .verifyComplete();
    }
    @Test
    public void testCreateUpdate() {
        var id = "fd31bca1ef884b4aa43d7cb053ccbf3a";
        Pauta pauta = new Pauta();
        pauta.setId(id);
        pauta.setTitle("Pauta 1");
        pauta.setDescription("Descrição da pauta 1");
        pauta.setDateStart(LocalDateTime.now());
        pauta.setDateEnd(LocalDateTime.now().plusMinutes(1)); // Pauta ativa por 1 minuto

        when(pautaRepository.existsById(id)).thenReturn(Mono.just(true));
        when(pautaRepository.save(pauta)).thenReturn(Mono.just(pauta));
        // Chame o método de criação da pauta
        Mono<Pauta> pautaMono = pautaService.update(id, pauta);

        // Verifique se a pauta foi criada corretamente
        StepVerifier.create(pautaMono)
                .expectNext(pauta) // Verifique se a pauta retornada é igual à pauta de exemplo
                .verifyComplete();
    }
    @Test
    public void testCreateUpdateIfIdNotExists() throws Exception {
        var id = "fd31bca1ef884b4aa43d7cb053ccbf3a";
        Pauta pauta = new Pauta();
        pauta.setId(id);
        pauta.setTitle("Pauta 1");
        pauta.setDescription("Descrição da pauta 1");
        pauta.setDateStart(LocalDateTime.now());
        pauta.setDateEnd(LocalDateTime.now().plusMinutes(1)); // Pauta ativa por 1 minuto

        when(pautaRepository.existsById(id)).thenReturn(Mono.just(false));
        when(pautaRepository.save(pauta)).thenReturn(Mono.just(pauta));
        // Chame o método de criação da pauta
        Mono<Pauta> pautaMono = pautaService.update(id, pauta);

        // Verifique se a pauta foi criada corretamente
        StepVerifier.create(pautaMono)
                .expectErrorMatches(throwable -> throwable.getMessage().equals("pauta não existe"))
                .verify();
    }
    @Test
    public void testVerifyPautasIsActive() {
        LocalDateTime dataInicio = LocalDateTime.now().minusMinutes(2); // Pauta ativa há mais de 1 minuto
        Pauta pauta = new Pauta();
        pauta.setDateStart(dataInicio);
        pauta.setAtivated(true);

        when(reactiveMongoTemplate.find(Mockito.any(), Mockito.eq(Pauta.class)))
                .thenReturn(Flux.just(pauta)); // Simula a busca de uma pauta ativa há mais de 1 minuto

        when(reactiveMongoTemplate.save(Mockito.any(Pauta.class)))
                .thenReturn(Mono.just(pauta)); // Simula a atualização da pauta

        StepVerifier.create(pautaService.verifyPautasIsActive())
                .expectNextMatches(updatedPauta -> !updatedPauta.isAtivated()) // Verifica se a pauta foi desativada
                .expectComplete()
                .verify();
    }
}
