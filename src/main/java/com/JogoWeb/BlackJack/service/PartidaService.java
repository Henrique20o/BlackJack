package com.JogoWeb.BlackJack.service;

import com.JogoWeb.BlackJack.dto.request.CriarPartidaRequest;
import com.JogoWeb.BlackJack.dto.request.EntrarPartidaRequest;
import com.JogoWeb.BlackJack.dto.response.PartidaResponse;
import com.JogoWeb.BlackJack.model.Jogador;
import com.JogoWeb.BlackJack.model.Partida;
import com.JogoWeb.BlackJack.model.StatusPartida;
import com.JogoWeb.BlackJack.repository.PartidaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PartidaService {

    private final PartidaRepository partidaRepository;
    private final JogadorService jogadorService;
    private final BaralhoService baralhoService;

    public PartidaService(
            PartidaRepository partidaRepository,
            JogadorService jogadorService,
            BaralhoService baralhoService
    ) {
        this.partidaRepository = partidaRepository;
        this.jogadorService = jogadorService;
        this.baralhoService = baralhoService;
    }

    public PartidaResponse criarPartida(CriarPartidaRequest request) {
        Jogador jogador = jogadorService.buscarJogadorPorId(request.getIdJogador());

        Partida partida = new Partida(
                request.getModoJogo(),
                baralhoService.criarBaralhoEmbaralhado()
        );

        partida.adicionarJogador(jogador);
        partida.setStatus(StatusPartida.AGUARDANDO_JOGADORES);

        partidaRepository.salvar(partida);

        return new PartidaResponse(partida);
    }

    public List<PartidaResponse> listarPartidasAguardandoJogadores() {
        return partidaRepository.listarAguardandoJogadores()
                .stream()
                .map(PartidaResponse::new)
                .toList();
    }

    private void distribuirCartasIniciais(Partida partida) {
        for (int i = 0; i < 2; i++) {
            for (Jogador jogador : partida.getJogadores()) {
                jogador.receberCarta(baralhoService.comprarCarta(partida.getBaralho()));
            }
        }
    }

    public PartidaResponse entrarNaPartida(UUID idPartida, EntrarPartidaRequest request) {
        Partida partida = partidaRepository.buscarPorId(idPartida)
                .orElseThrow(() -> new RuntimeException("Partida não encontrada."));

        Jogador jogador = jogadorService.buscarJogadorPorId(request.getIdJogador());

        if (!partida.podeEntrar()) {
            throw new RuntimeException("Não é possível entrar nesta partida.");
        }

        if (partida.jogadorEstaNaPartida(jogador.getId())) {
            throw new RuntimeException("Este jogador já está na partida.");
        }

        partida.adicionarJogador(jogador);

        if (partida.estaCheia()) {
            partida.setStatus(StatusPartida.EM_ANDAMENTO);
            distribuirCartasIniciais(partida);
            partida.setJogadorAtualId(partida.getJogadores().get(0).getId());
        }

        partidaRepository.salvar(partida);

        return new PartidaResponse(partida);
    }
}