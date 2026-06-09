package com.JogoWeb.BlackJack.repository;

import com.JogoWeb.BlackJack.model.Jogador;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JogadorRepository {

    private final Map<UUID, Jogador> jogadores = new HashMap<>();

    public Jogador salvar(Jogador jogador) {
        jogadores.put(jogador.getId(), jogador);
        return jogador;
    }

    public Optional<Jogador> buscarPorId(UUID id) {
        return Optional.ofNullable(jogadores.get(id));
    }

    public List<Jogador> listarTodos() {
        return new ArrayList<>(jogadores.values());
    }

    public boolean existePorId(UUID id) {
        return jogadores.containsKey(id);
    }
}