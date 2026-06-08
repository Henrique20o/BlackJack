package com.JogoWeb.BlackJack.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Carta {

    private Naipe naipe;
    private ValorCarta valor;

    public int getPontos() {
        return valor.getPontos();
    }
}