package com.JogoWeb.BlackJack.dto.response;

import com.JogoWeb.BlackJack.model.Jogador;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class JogadorPartidaResponse {

    private UUID id;
    private String nome;
    private List<CartaResponse> mao;
    private int pontuacao;
    private boolean parou;
    private boolean estourou;

    public JogadorPartidaResponse(Jogador jogador) {
        this.id = jogador.getId();
        this.nome = jogador.getNome();
        this.mao = jogador.getMao()
                .stream()
                .map(CartaResponse::new)
                .toList();
        this.pontuacao = jogador.calcularPontuacao();
        this.parou = jogador.isParou();
        this.estourou = jogador.isEstourou();
    }
}