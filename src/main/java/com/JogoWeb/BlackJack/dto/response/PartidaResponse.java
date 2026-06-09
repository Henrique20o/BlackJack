package com.JogoWeb.BlackJack.dto.response;

import com.JogoWeb.BlackJack.model.Partida;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
public class PartidaResponse {

    private UUID id;
    private String status;
    private String modoJogo;
    private UUID jogadorAtualId;
    private UUID vencedorId;
    private boolean empateRound;
    private boolean empatePartida;
    private int cartasRestantes;
    private int quantidadeRounds;
    private int roundAtual;
    private Map<UUID, Integer> placar;
    private List<JogadorPartidaResponse> jogadores;
    private UUID vencedorPartidaId;
    private int quantidadeMaximaJogadores;

    public PartidaResponse(Partida partida) {
        this.id = partida.getId();
        this.status = partida.getStatus().name();
        this.modoJogo = partida.getModoJogo().name();
        this.jogadorAtualId = partida.getJogadorAtualId();
        this.vencedorId = partida.getVencedorId();
        this.empateRound = partida.isEmpateRound();
        this.empatePartida = partida.isEmpatePartida();
        this.cartasRestantes = partida.getBaralho().size();
        this.quantidadeRounds = partida.getQuantidadeRounds();
        this.roundAtual = partida.getRoundAtual();
        this.placar = partida.getPlacar();
        this.quantidadeMaximaJogadores = partida.getQuantidadeMaximaJogadores();
        this.vencedorPartidaId = partida.getVencedorPartidaId();
        this.jogadores = partida.getJogadores()
                .stream()
                .map(JogadorPartidaResponse::new)
                .toList();
    }
}