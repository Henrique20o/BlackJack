package com.JogoWeb.BlackJack.model;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

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

    private int quantidadeRounds;
    private int roundAtual;

    private Map<UUID, Integer> placar;
    private UUID vencedorPartidaId;

    private boolean empateRound;
    private boolean empatePartida;

    private int quantidadeMaximaJogadores;
    private int indiceJogadorInicialRound;

    public Partida(
            ModoJogo modoJogo,
            Stack<Carta> baralho,
            int quantidadeRounds,
            int quantidadeMaximaJogadores
    ) {
        this.id = UUID.randomUUID();
        this.jogadores = new ArrayList<>();
        this.baralho = baralho;
        this.status = StatusPartida.AGUARDANDO_JOGADORES;
        this.modoJogo = modoJogo;

        this.quantidadeRounds = quantidadeRounds;
        this.roundAtual = 1;

        this.quantidadeMaximaJogadores = quantidadeMaximaJogadores;
        this.indiceJogadorInicialRound = 0;

        this.placar = new HashMap<>();
        this.vencedorPartidaId = null;

        this.empateRound = false;
        this.empatePartida = false;
    }

    public void adicionarJogador(Jogador jogador) {
        Jogador jogadorDaPartida = new Jogador(
                jogador.getId(),
                jogador.getNome()
        );

        this.jogadores.add(jogadorDaPartida);
        this.placar.put(jogador.getId(), 0);
    }

    public boolean estaCheia() {
        return jogadores.size() >= quantidadeMaximaJogadores;
    }

    public Jogador buscarJogadorPorId(UUID idJogador) {
        for (Jogador jogador : jogadores) {
            if (jogador.getId().equals(idJogador)) {
                return jogador;
            }
        }

        return null;
    }

    public boolean jogadorEstaNaPartida(UUID idJogador) {
        return jogadores.stream()
                .anyMatch(jogador -> jogador.getId().equals(idJogador));
    }

    public boolean podeEntrar() {
        return status == StatusPartida.AGUARDANDO_JOGADORES && !estaCheia();
    }

    public void passarTurno() {
        if (todosJogadoresFinalizaram()) {
            jogadorAtualId = null;
            return;
        }

        int indiceAtual = -1;

        for (int i = 0; i < jogadores.size(); i++) {
            if (jogadores.get(i).getId().equals(jogadorAtualId)) {
                indiceAtual = i;
                break;
            }
        }

        for (int i = 1; i <= jogadores.size(); i++) {
            int proximoIndice = (indiceAtual + i) % jogadores.size();
            Jogador proximoJogador = jogadores.get(proximoIndice);

            if (!proximoJogador.isParou() && !proximoJogador.isEstourou()) {
                jogadorAtualId = proximoJogador.getId();
                return;
            }
        }

        jogadorAtualId = null;
    }

    public boolean todosJogadoresFinalizaram() {
        return jogadores.stream()
                .allMatch(jogador -> jogador.isParou() || jogador.isEstourou());
    }

    public Jogador getJogadorAtual() {
        return buscarJogadorPorId(jogadorAtualId);
    }

    public void adicionarPontoParaJogador(UUID idJogador) {
        int pontosAtuais = placar.getOrDefault(idJogador, 0);
        placar.put(idJogador, pontosAtuais + 1);
    }

    public void avancarJogadorInicialRound() {
        this.indiceJogadorInicialRound =
                (this.indiceJogadorInicialRound + 1) % jogadores.size();
    }

    public UUID getIdJogadorInicialRound() {
        return jogadores.get(indiceJogadorInicialRound).getId();
    }
}