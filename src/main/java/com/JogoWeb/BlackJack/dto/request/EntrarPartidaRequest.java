package com.JogoWeb.BlackJack.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class EntrarPartidaRequest {

    @NotNull(message = "O ID do jogador é obrigatório.")
    private UUID idJogador;
}