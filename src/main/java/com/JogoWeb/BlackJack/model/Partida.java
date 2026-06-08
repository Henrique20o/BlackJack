package com.JogoWeb.BlackJack.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.UUID;

@Getter
@Setter
public class Partida {

    private UUID id;
    private List<Jogador> jogadores;
    private Jogador dealer;
    private Stack<Carta> baralho;
    private StatusPartida status;
    private ModoJogo modoJogo;
    private UUID jogadorAtualId;
    private UUID vencedorId;
    private boolean empate;

    public Partida(ModoJogo modoJogo, Stack<Carta> baralho) {
        this.id = UUID.randomUUID();
        this.jogadores = new ArrayList<>();
        this.baralho = baralho;
        this.status = StatusPartida.AGUARDANDO_JOGADORES;
        this.modoJogo = modoJogo;
        this.empate = false;
    }

    public void adicionarJogador(Jogador jogador) {
        this.jogadores.add(jogador);
    }

    public boolean estaCheia() {
        if (modoJogo == ModoJogo.JOGADOR_VS_JOGADOR) {
            return jogadores.size() == 2;
        }

        if (modoJogo == ModoJogo.JOGADORES_VS_DEALER) {
            return jogadores.size() >= 1;
        }

        return false;
    }

    public Jogador buscarJogadorPorId(UUID idJogador) {
        for (Jogador jogador : jogadores) {
            if (jogador.getId().equals(idJogador)) {
                return jogador;
            }
        }

        return null;
    }
}