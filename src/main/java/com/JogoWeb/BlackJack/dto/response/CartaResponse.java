package com.JogoWeb.BlackJack.dto.response;

import com.JogoWeb.BlackJack.model.Carta;

public class CartaResponse {

    private String naipe;
    private String valor;
    private int pontos;

    public CartaResponse(Carta carta) {
        this.naipe = carta.getNaipe().name();
        this.valor = carta.getValor().name();
        this.pontos = carta.getPontos();
    }

    public String getNaipe() {
        return naipe;
    }

    public String getValor() {
        return valor;
    }

    public int getPontos() {
        return pontos;
    }
}