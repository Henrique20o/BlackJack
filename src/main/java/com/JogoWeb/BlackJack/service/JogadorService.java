package com.JogoWeb.BlackJack.service;

import com.JogoWeb.BlackJack.dto.request.CriarJogadorRequest;
import com.JogoWeb.BlackJack.dto.response.JogadorResponse;
import com.JogoWeb.BlackJack.model.Jogador;
import com.JogoWeb.BlackJack.repository.JogadorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class JogadorService {

    private final JogadorRepository jogadorRepository;

    public JogadorService(JogadorRepository jogadorRepository) {
        this.jogadorRepository = jogadorRepository;
    }

    public JogadorResponse criarJogador(CriarJogadorRequest request) {
        Jogador jogador = new Jogador(request.getNome());

        jogadorRepository.salvar(jogador);

        return new JogadorResponse(
                jogador.getId(),
                jogador.getNome()
        );
    }

    public Jogador buscarJogadorPorId(UUID id) {
        return jogadorRepository.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Jogador não encontrado."));
    }

    public List<Jogador> listarTodos() {
        return jogadorRepository.listarTodos();
    }
}