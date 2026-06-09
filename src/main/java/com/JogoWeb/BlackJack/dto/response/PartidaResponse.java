package com.JogoWeb.BlackJack.dto.response;

import com.JogoWeb.BlackJack.model.Partida;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class PartidaResponse {

    private UUID id;
    private String status;
    private String modoJogo;
    private UUID jogadorAtualId;
    private UUID vencedorId;
    private boolean empate;
    private int cartasRestantes;
    private List<JogadorPartidaResponse> jogadores;

    public PartidaResponse(Partida partida) {
        this.id = partida.getId();
        this.status = partida.getStatus().name();
        this.modoJogo = partida.getModoJogo().name();
        this.jogadorAtualId = partida.getJogadorAtualId();
        this.vencedorId = partida.getVencedorId();
        this.empate = partida.isEmpate();
        this.cartasRestantes = partida.getBaralho().size();
        this.jogadores = partida.getJogadores()
                .stream()
                .map(JogadorPartidaResponse::new)
                .toList();
    }
}