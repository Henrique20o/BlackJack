package com.JogoWeb.BlackJack.repository;

import com.JogoWeb.BlackJack.model.Partida;
import com.JogoWeb.BlackJack.model.StatusPartida;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PartidaRepository {

    private final Map<UUID, Partida> partidas = new HashMap<>();

    public Partida salvar(Partida partida) {
        partidas.put(partida.getId(), partida);
        return partida;
    }

    public Optional<Partida> buscarPorId(UUID id) {
        return Optional.ofNullable(partidas.get(id));
    }

    public List<Partida> listarTodas() {
        return new ArrayList<>(partidas.values());
    }

    public List<Partida> listarAguardandoJogadores() {
        return partidas.values()
                .stream()
                .filter(partida -> partida.getStatus() == StatusPartida.AGUARDANDO_JOGADORES)
                .toList();
    }

    public boolean existePorId(UUID id) {
        return partidas.containsKey(id);
    }
}