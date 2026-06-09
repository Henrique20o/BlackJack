package com.JogoWeb.BlackJack.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CriarJogadorRequest {

    @NotBlank(message = "O nome do jogador é obrigatório.")
    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}