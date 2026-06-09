package com.JogoWeb.BlackJack.controller;

import com.JogoWeb.BlackJack.dto.request.CriarPartidaRequest;
import com.JogoWeb.BlackJack.dto.request.EntrarPartidaRequest;
import com.JogoWeb.BlackJack.dto.request.JogadaRequest;
import com.JogoWeb.BlackJack.dto.response.PartidaResponse;
import com.JogoWeb.BlackJack.service.PartidaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/partidas")
public class PartidaController {

    private final PartidaService partidaService;

    public PartidaController(PartidaService partidaService) {
        this.partidaService = partidaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PartidaResponse criarPartida(@Valid @RequestBody CriarPartidaRequest request) {
        return partidaService.criarPartida(request);
    }
    @GetMapping
    public List<PartidaResponse> listarPartidasAguardandoJogadores() {
        return partidaService.listarPartidasAguardandoJogadores();
    }

    @PostMapping("/{id}/entrar")
    public PartidaResponse entrarNaPartida(
            @PathVariable UUID id,
            @Valid @RequestBody EntrarPartidaRequest request
    ) {
        return partidaService.entrarNaPartida(id, request);
    }
    @GetMapping("/{id}")
    public PartidaResponse buscarPartidaPorId(@PathVariable UUID id) {
        return partidaService.buscarPartidaPorId(id);
    }

    @PostMapping("/{id}/jogadas")
    public PartidaResponse realizarJogada(
            @PathVariable UUID id,
            @Valid @RequestBody JogadaRequest request
    ) {
        return partidaService.realizarJogada(id, request);
    }
    @PostMapping("/{id}/proximo-round")
    public PartidaResponse iniciarProximoRound(@PathVariable UUID id) {
        return partidaService.iniciarProximoRound(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarPartida(@PathVariable UUID id) {
        partidaService.deletarPartida(id);
    }
}