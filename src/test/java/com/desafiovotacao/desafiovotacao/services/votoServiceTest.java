package com.desafiovotacao.desafiovotacao.services;

import com.desafiovotacao.desafiovotacao.entities.Pauta;
import com.desafiovotacao.desafiovotacao.entities.Usuario;
import com.desafiovotacao.desafiovotacao.entities.Voto;
import com.desafiovotacao.desafiovotacao.repositories.PautaRepository;
import com.desafiovotacao.desafiovotacao.repositories.UsuarioRepository;
import com.desafiovotacao.desafiovotacao.repositories.VotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.Arrays;
import org.springframework.data.mongodb.core.query.Query;
import static org.mockito.Mockito.eq;
@SpringBootTest
    public class votoServiceTest {
    @Mock
    private PautaService pautaService;
    @Mock
    PautaRepository pautaRepository;
    @Mock
    private ReactiveMongoTemplate reactiveMongoTemplate;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testFindAllByPautaId() {
        var usuarioService = new UsuarioService(usuarioRepository,reactiveMongoTemplate);
        var pautaServiceInstance = new PautaService(pautaRepository, reactiveMongoTemplate);
        var votoServiceInstance = new VotoService(pautaServiceInstance, usuarioService);


        Pauta pauta = new Pauta();
        pauta.setId("pautaId");
        pauta.setVotos(Arrays.asList(
                new Voto("votoId1", true, LocalDateTime.now(), "pautaId","userId", new Usuario()),
                new Voto("votoId2",  false, LocalDateTime.now(), "pautaId", "userId",new Usuario())
        ));

        // Configure o comportamento do pautaService.findPautabyId para retornar a pauta criada
        when(pautaRepository.findById("pautaId")).thenReturn(Mono.just(pauta));

        // Chame o método que você está testando
        Flux<Voto> result = votoServiceInstance.findAllByPautaId("pautaId");

        // Verifique se o fluxo de votos foi emitido corretamente
        StepVerifier.create(result)
                .expectNextMatches(voto -> voto.getId().equals("votoId1"))
                .expectNextMatches(voto -> voto.getId().equals("votoId2"))
                .verifyComplete();
    }
    @Test
    public void testCreateVoteSuccess() throws Exception {
        var usuarioService = new UsuarioService(usuarioRepository,reactiveMongoTemplate);
        // Configurar pautaService.findPautabyId para retornar uma pauta válida
        var pautaServiceInstance = new PautaService(pautaRepository, reactiveMongoTemplate);
        var votoServiceInstance = new VotoService(pautaServiceInstance, usuarioService);
        Pauta pauta = new Pauta();
        Voto voto = new Voto();
        Usuario usuario = new Usuario();
        usuario.setCpf("cpf3");

        voto.setUsuario(usuario);
        pauta.setId("pautaId");
        pauta.setTitle("Pauta de Teste");
        pauta.setDateStart(LocalDateTime.now());
        pauta.setDateEnd(LocalDateTime.now().plusMinutes(1));

        Usuario newUsuario = new Usuario();
        newUsuario.setCpf("cpf3");

        pauta.setVotos(Arrays.asList(voto)); // Pauta sem votos

        when(pautaRepository.findById("pautaId")).thenReturn(Mono.just(pauta));
        when(pautaRepository.existsById("pautaId")).thenReturn(Mono.just(true));
        when(pautaRepository.save(pauta)).thenReturn(Mono.just(pauta));
        when(reactiveMongoTemplate.findOne(
                Query.query(Criteria.where("cpf").is("cpf2")),Usuario.class
        )).thenReturn(Mono.just(usuario));
        Mono<Pauta> result = votoServiceInstance.create("pautaId", "cpf2", "sim");

        StepVerifier.create(result)
                .expectNext(pauta)
                .verifyComplete();
    }
    @Test
    public void testCreateVotoFaillWithMinutes() throws Exception {

        // Configurar pautaService.findPautabyId para retornar uma pauta válida
        var usuarioService = new UsuarioService(usuarioRepository,reactiveMongoTemplate);
        var pautaServiceInstance = new PautaService(pautaRepository, reactiveMongoTemplate);
        var votoServiceInstance = new VotoService(pautaServiceInstance,usuarioService );
        Pauta pauta = new Pauta();
        Voto voto = new Voto();
        Usuario usuario = new Usuario();
        usuario.setCpf("cpf2");
        voto.setUsuario(usuario);
        pauta.setId("pautaId");
        pauta.setTitle("Pauta de Teste");
        pauta.setDateStart(LocalDateTime.now().minusMinutes(1));
        pauta.setDateEnd(LocalDateTime.now().plusMinutes(1));

        pauta.setVotos(Arrays.asList(voto)); // Pauta sem votos

        when(pautaRepository.findById("pautaId")).thenReturn(Mono.just(pauta));
        when(pautaRepository.existsById("pautaId")).thenReturn(Mono.just(true));
        when(pautaRepository.save(pauta)).thenReturn(Mono.just(pauta));
        Mono<Pauta> result = votoServiceInstance.create("pautaId", "cpf", "sim");
        // Configurar pautaService.update prara retornar a pauta atualizada

        // Chamar o método que você está testando


        // Verificar se a criação do voto foi bem-sucedida
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable.getMessage().equals("O Horario de votação finalizou para a pauta "+ pauta.getTitle()))
                .verify(); // Verifica se as expectativas foram atendidas
    }
    @Test
    public void testCreateVotoFaillUserExists() throws Exception {

        var usuarioService = new UsuarioService(usuarioRepository,reactiveMongoTemplate);
        var pautaServiceInstance = new PautaService(pautaRepository, reactiveMongoTemplate);
        var votoServiceInstance = new VotoService(pautaServiceInstance, usuarioService);
        Pauta pauta = new Pauta();
        Voto voto = new Voto();
        Usuario usuario = new Usuario();
        usuario.setCpf("cpf");

        // Configurar o comportamento esperado para o mock do Usuario
//        when(usuario.getCpf()).thenReturn("cpf");

        voto.setUsuario(usuario);
        pauta.setId("pautaId");
        pauta.setTitle("Pauta de Teste");
        pauta.setDateStart(LocalDateTime.now());
        pauta.setDateEnd(LocalDateTime.now().plusMinutes(1));

        pauta.setVotos(Arrays.asList(voto)); // Pauta sem votos

        when(pautaRepository.findById("pautaId")).thenReturn(Mono.just(pauta));
        when(pautaRepository.existsById("pautaId")).thenReturn(Mono.just(true));
        when(pautaRepository.save(pauta)).thenReturn(Mono.just(pauta));
        Mono<Pauta> result = votoServiceInstance.create("pautaId", "cpf", "sim");
        // Configurar pautaService.update prara retornar a pauta atualizada

        // Chamar o método que você está testando


        // Verificar se a criação do voto foi bem-sucedida
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable.getMessage().equals("Usuario já votou"))
                .verify(); // Verifica se as expectativas foram atendidas
    }
}
