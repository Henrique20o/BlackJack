package com.JogoWeb.BlackJack.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Jogador {

    private UUID id;
    private String nome;
    private List<Carta> mao;
    private boolean parou;
    private boolean estourou;

    public Jogador(String nome) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.mao = new ArrayList<>();
        this.parou = false;
        this.estourou = false;
    }

    public Jogador(UUID id, String nome) {
        this.id = id;
        this.nome = nome;
        this.mao = new ArrayList<>();
        this.parou = false;
        this.estourou = false;
    }

    public void receberCarta(Carta carta) {
        this.mao.add(carta);

        if (calcularPontuacao() > 21) {
            this.estourou = true;
        }
    }

    public int calcularPontuacao() {
        int total = 0;

        for (Carta carta : mao) {
            total += carta.getPontos();
        }

        return total;
    }
}