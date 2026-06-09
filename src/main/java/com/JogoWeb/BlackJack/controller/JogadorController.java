package com.JogoWeb.BlackJack.controller;

import com.JogoWeb.BlackJack.dto.request.CriarJogadorRequest;
import com.JogoWeb.BlackJack.dto.response.JogadorResponse;
import com.JogoWeb.BlackJack.service.JogadorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jogadores")
public class JogadorController {

    private final JogadorService jogadorService;

    public JogadorController(JogadorService jogadorService) {
        this.jogadorService = jogadorService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JogadorResponse criarJogador(@Valid @RequestBody CriarJogadorRequest request) {
        return jogadorService.criarJogador(request);
    }
}