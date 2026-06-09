package com.JogoWeb.BlackJack.dto.request;

import com.JogoWeb.BlackJack.model.ModoJogo;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class CriarPartidaRequest {

    @NotNull(message = "O ID do jogador é obrigatório.")
    private UUID idJogador;

    @NotNull(message = "O modo de jogo é obrigatório.")
    private ModoJogo modoJogo;

    public UUID getIdJogador() {
        return idJogador;
    }

    public void setIdJogador(UUID idJogador) {
        this.idJogador = idJogador;
    }

    public ModoJogo getModoJogo() {
        return modoJogo;
    }

    public void setModoJogo(ModoJogo modoJogo) {
        this.modoJogo = modoJogo;
    }
}