package com.JogoWeb.BlackJack.dto.request;

import com.JogoWeb.BlackJack.model.ModoJogo;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class CriarPartidaRequest {

    @NotNull(message = "O ID do jogador é obrigatório.")
    private UUID idJogador;

    @NotNull(message = "O modo de jogo é obrigatório.")
    private ModoJogo modoJogo;

    @Min(value = 1, message = "A partida deve ter pelo menos 1 round.")
    @Max(value = 10, message = "A partida pode ter no máximo 10 rounds.")
    private int quantidadeRounds = 1;

    @Min(value = 2, message = "A partida deve ter pelo menos 2 jogadores.")
    @Max(value = 5, message = "A partida pode ter no máximo 5 jogadores.")
    private int quantidadeMaximaJogadores = 2;

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

    public int getQuantidadeRounds() {
        return quantidadeRounds;
    }

    public void setQuantidadeRounds(int quantidadeRounds) {
        this.quantidadeRounds = quantidadeRounds;
    }

    public int getQuantidadeMaximaJogadores() {
        return quantidadeMaximaJogadores;
    }

    public void setQuantidadeMaximaJogadores(int quantidadeMaximaJogadores) {
        this.quantidadeMaximaJogadores = quantidadeMaximaJogadores;
    }
}