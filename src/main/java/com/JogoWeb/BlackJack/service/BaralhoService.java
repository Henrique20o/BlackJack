package com.JogoWeb.BlackJack.service;

import com.JogoWeb.BlackJack.model.Carta;
import  com.JogoWeb.BlackJack.model.Naipe;
import  com.JogoWeb.BlackJack.model.ValorCarta;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

@Service
public class BaralhoService {

    public Stack<Carta> criarBaralhoEmbaralhado() {
        List<Carta> cartas = new ArrayList<>();

        for (Naipe naipe : Naipe.values()) {
            for (ValorCarta valor : ValorCarta.values()) {
                Carta carta = new Carta(naipe, valor);
                cartas.add(carta);
            }
        }

        Collections.shuffle(cartas);

        Stack<Carta> baralho = new Stack<>();

        for (Carta carta : cartas) {
            baralho.push(carta);
        }

        return baralho;
    }

    public Carta comprarCarta(Stack<Carta> baralho) {
        if (baralho.isEmpty()) {
            throw new IllegalStateException("O baralho está vazio.");
        }

        return baralho.pop();
    }
}
