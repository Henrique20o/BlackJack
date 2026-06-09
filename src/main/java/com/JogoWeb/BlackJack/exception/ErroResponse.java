package com.JogoWeb.BlackJack.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErroResponse {

  private LocalDateTime timestamp;
  private int status;
  private String erro;
  private String mensagem;
  private String caminho;
}