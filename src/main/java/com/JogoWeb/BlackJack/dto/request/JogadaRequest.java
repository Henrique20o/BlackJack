package com.JogoWeb.BlackJack.dto.request;

import com.JogoWeb.BlackJack.model.AcaoJogada;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class JogadaRequest {

    @NotNull(message = "O ID do jogador é obrigatório.")
    private UUID idJogador;

    @NotNull(message = "A ação da jogada é obrigatória.")
    private AcaoJogada acao;
}