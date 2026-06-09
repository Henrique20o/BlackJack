package com.JogoWeb.BlackJack.dto.response;

import java.util.UUID;

public class JogadorResponse {

    private UUID id;
    private String nome;

    public JogadorResponse(UUID id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}