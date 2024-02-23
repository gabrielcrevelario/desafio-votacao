package com.desafiovotacao.desafiovotacao.services;

import com.desafiovotacao.desafiovotacao.entities.Pauta;
import com.desafiovotacao.desafiovotacao.entities.Usuario;
import com.desafiovotacao.desafiovotacao.entities.Voto;
import com.desafiovotacao.desafiovotacao.repositories.PautaRepository;
import com.desafiovotacao.desafiovotacao.repositories.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class VotoService {
    private PautaService pautaService;
    private UsuarioService usuarioService;

    @Autowired
    public VotoService(PautaService pautaService, UsuarioService usuarioService) {
        this.pautaService = pautaService;
        this.usuarioService = usuarioService;
    }

    public Flux<Voto> findAllByPautaId(String pautaId) {
        return pautaService.findPautabyId(pautaId)
                .flatMapMany(pauta -> Flux.fromIterable(pauta.getVotos()));
    }

    public Mono<Pauta> create(String pautaId, String cpf, String voto) throws Exception {
        try {
            return this.pautaService.findPautabyId(pautaId).flatMap(p -> {
                if (p != null) {
                    var currentTime = LocalDateTime.now();
                    LocalDateTime dataInicio = p.getDateStart();
                    Duration duration = Duration.between(dataInicio, currentTime);
                    return completedVoto(dataInicio, pautaId, cpf, voto).flatMap(nv -> verifyDurationPauta(pautaId, cpf, nv, p, duration));
                } else return Mono.error(new Exception("Essa pauta não existe"));

            });
        } catch (Exception ex) {
            throw ex;
        }
    }

    private Mono<Voto> completedVoto(LocalDateTime dataInicio, String pautaId, String cpf, String voto) {
        return usuarioService.findByCpf(cpf).flatMap(f -> {
            if (f != null) {
                var newVoto = new Voto();
                newVoto.setPautaId(pautaId);
                newVoto.setUsuario(f);
                newVoto.setUserId(f.getId());
                newVoto.setVoto(voto.equalsIgnoreCase("sim"));
                newVoto.setDateStart(dataInicio);
                return Mono.just(newVoto);
            } else return Mono.error(new Exception("Usuario não existe"));
        });
    }


    private Mono<Pauta> verifyDurationPauta(String pautaId, String cpf, Voto voto, Pauta p, Duration duration) {
        if (duration.compareTo(Duration.ofMinutes(1)) < 0) {
            return verifyUserIfVoted(pautaId, cpf, voto, p);

        } else {
            return Mono.error(new Exception("O Horario de votação finalizou para a pauta " + p.getTitle()));
        }
    }

    private Mono<Pauta> verifyUserIfVoted(String pautaId, String cpf, Voto voto, Pauta p) {
        var votosNotExists = Optional.ofNullable(p.getVotos()).isEmpty();
        Optional<Voto> alreadyVoted
                = !votosNotExists ? p.getVotos().stream().filter(f -> f.getUsuario().getCpf().equals(cpf)).findFirst() : null;
        if (votosNotExists || alreadyVoted.isEmpty()) {
            if(votosNotExists) p.setVotos(new ArrayList<>());
            p.getVotos().add(voto);
            return pautaService.update(pautaId, p);
        } else {
            return Mono.error(new Exception("Usuario já votou"));
        }
    }

}
