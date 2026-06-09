package com.JogoWeb.BlackJack.service;

import com.JogoWeb.BlackJack.dto.request.CriarPartidaRequest;
import com.JogoWeb.BlackJack.dto.request.EntrarPartidaRequest;
import com.JogoWeb.BlackJack.dto.request.JogadaRequest;
import com.JogoWeb.BlackJack.dto.response.PartidaResponse;
import com.JogoWeb.BlackJack.exception.RecursoNaoEncontradoException;
import com.JogoWeb.BlackJack.exception.RegraDeNegocioException;
import com.JogoWeb.BlackJack.model.Jogador;
import com.JogoWeb.BlackJack.model.Partida;
import com.JogoWeb.BlackJack.model.StatusPartida;
import com.JogoWeb.BlackJack.repository.PartidaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PartidaService {

    private final PartidaRepository partidaRepository;
    private final JogadorService jogadorService;
    private final BaralhoService baralhoService;

    public PartidaService(
            PartidaRepository partidaRepository,
            JogadorService jogadorService,
            BaralhoService baralhoService
    ) {
        this.partidaRepository = partidaRepository;
        this.jogadorService = jogadorService;
        this.baralhoService = baralhoService;
    }

    public PartidaResponse criarPartida(CriarPartidaRequest request) {
        Jogador jogador = jogadorService.buscarJogadorPorId(request.getIdJogador());

        Partida partida = new Partida(
                request.getModoJogo(),
                baralhoService.criarBaralhoEmbaralhado(),
                request.getQuantidadeRounds(),
                request.getQuantidadeMaximaJogadores()
        );

        partida.adicionarJogador(jogador);
        partida.setStatus(StatusPartida.AGUARDANDO_JOGADORES);

        partidaRepository.salvar(partida);

        return new PartidaResponse(partida);
    }

    public List<PartidaResponse> listarPartidasAguardandoJogadores() {
        return partidaRepository.listarAguardandoJogadores()
                .stream()
                .map(PartidaResponse::new)
                .toList();
    }

    private void distribuirCartasIniciais(Partida partida) {
        for (int i = 0; i < 2; i++) {
            for (Jogador jogador : partida.getJogadores()) {
                jogador.receberCarta(baralhoService.comprarCarta(partida.getBaralho()));
            }
        }
    }

    public PartidaResponse entrarNaPartida(UUID idPartida, EntrarPartidaRequest request) {
        Partida partida = partidaRepository.buscarPorId(idPartida)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Partida não encontrada."));

        Jogador jogador = jogadorService.buscarJogadorPorId(request.getIdJogador());

        if (!partida.podeEntrar()) {
            throw new RegraDeNegocioException("Não é possível entrar nesta partida.");
        }

        if (partida.jogadorEstaNaPartida(jogador.getId())) {
            throw new RegraDeNegocioException("Este jogador já está na partida.");
        }

        partida.adicionarJogador(jogador);

        if (partida.estaCheia()) {
            partida.setStatus(StatusPartida.EM_ANDAMENTO);
            distribuirCartasIniciais(partida);
            definirJogadorInicialDoRound(partida);
        }

        partidaRepository.salvar(partida);

        return new PartidaResponse(partida);
    }
    public PartidaResponse buscarPartidaPorId(UUID idPartida) {
        Partida partida = partidaRepository.buscarPorId(idPartida)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Partida não encontrada."));
        return new PartidaResponse(partida);
    }
    public PartidaResponse realizarJogada(UUID idPartida, JogadaRequest request) {
        Partida partida = partidaRepository.buscarPorId(idPartida)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Partida não encontrada."));

        if (partida.getStatus() != StatusPartida.EM_ANDAMENTO) {
            throw new RegraDeNegocioException("A partida não está em andamento.");
        }

        Jogador jogador = partida.buscarJogadorPorId(request.getIdJogador());

        if (jogador == null) {
            throw new RegraDeNegocioException("Jogador não pertence a esta partida.");
        }

        if (!partida.getJogadorAtualId().equals(jogador.getId())) {
            throw new RegraDeNegocioException("Não é a vez deste jogador.");
        }

        if (jogador.isParou() || jogador.isEstourou()) {
            throw new RegraDeNegocioException("Este jogador não pode mais jogar.");
        }

        switch (request.getAcao()) {
            case COMPRAR -> {
                comprarCarta(partida, jogador);

                if (jogador.isEstourou()) {
                    passarTurno(partida);
                }
            }

            case PARAR -> {
                jogador.setParou(true);
                passarTurno(partida);
            }
        }

        verificarFimDaPartida(partida);

        partidaRepository.salvar(partida);

        return new PartidaResponse(partida);
    }

    private void comprarCarta(Partida partida, Jogador jogador) {
        jogador.receberCarta(
                baralhoService.comprarCarta(partida.getBaralho())
        );
    }

    private void passarTurno(Partida partida) {
        List<Jogador> jogadores = partida.getJogadores();

        int indiceAtual = -1;

        for (int i = 0; i < jogadores.size(); i++) {
            if (jogadores.get(i).getId().equals(partida.getJogadorAtualId())) {
                indiceAtual = i;
                break;
            }
        }

        for (int i = 1; i <= jogadores.size(); i++) {
            int proximoIndice = (indiceAtual + i) % jogadores.size();
            Jogador proximoJogador = jogadores.get(proximoIndice);

            if (!proximoJogador.isParou() && !proximoJogador.isEstourou()) {
                partida.setJogadorAtualId(proximoJogador.getId());
                return;
            }
        }

        partida.setJogadorAtualId(null);
    }

    private void verificarFimDaPartida(Partida partida) {
        boolean todosEncerraram = partida.getJogadores()
                .stream()
                .allMatch(jogador -> jogador.isParou() || jogador.isEstourou());

        if (!todosEncerraram) {
            return;
        }

        Jogador vencedorRound = definirVencedorDoRound(partida);

        partida.setJogadorAtualId(null);

        if (vencedorRound == null) {
            partida.setEmpateRound(true);
            partida.setVencedorId(null);
        } else {
            partida.setEmpateRound(false);
            partida.setVencedorId(vencedorRound.getId());
            partida.adicionarPontoParaJogador(vencedorRound.getId());
        }

        if (partida.getRoundAtual() < partida.getQuantidadeRounds()) {
            partida.setStatus(StatusPartida.ROUND_FINALIZADO);
        } else {
            partida.setStatus(StatusPartida.FINALIZADA);
            definirVencedorDaPartida(partida);
        }
    }

    private Jogador definirVencedorDoRound(Partida partida) {
        Jogador vencedor = null;
        int melhorPontuacao = 0;
        boolean empate = false;

        for (Jogador jogador : partida.getJogadores()) {
            int pontuacao = jogador.calcularPontuacao();

            if (pontuacao > 21) {
                continue;
            }

            if (pontuacao > melhorPontuacao) {
                melhorPontuacao = pontuacao;
                vencedor = jogador;
                empate = false;
            } else if (pontuacao == melhorPontuacao && vencedor != null) {
                empate = true;
            }
        }

        if (empate) {
            return null;
        }

        return vencedor;
    }

    private void definirVencedorDaPartida(Partida partida) {
        UUID vencedorPartidaId = null;
        int maiorPontuacao = -1;
        boolean empateFinal = false;

        for (var entrada : partida.getPlacar().entrySet()) {
            UUID idJogador = entrada.getKey();
            int pontos = entrada.getValue();

            if (pontos > maiorPontuacao) {
                maiorPontuacao = pontos;
                vencedorPartidaId = idJogador;
                empateFinal = false;
            } else if (pontos == maiorPontuacao) {
                empateFinal = true;
            }
        }

        if (empateFinal) {
            partida.setEmpatePartida(true);
            partida.setVencedorPartidaId(null);
        } else {
            partida.setEmpatePartida(false);
            partida.setVencedorPartidaId(vencedorPartidaId);
        }
    }
    public PartidaResponse iniciarProximoRound(UUID idPartida) {
        Partida partida = partidaRepository.buscarPorId(idPartida)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Partida não encontrada."));

        if (partida.getStatus() != StatusPartida.ROUND_FINALIZADO) {
            throw new RegraDeNegocioException("Só é possível iniciar o próximo round após o round atual ser finalizado.");
        }

        if (partida.getRoundAtual() >= partida.getQuantidadeRounds()) {
            throw new RegraDeNegocioException("Não existem mais rounds para iniciar.");
        }

        partida.setRoundAtual(partida.getRoundAtual() + 1);

        partida.avancarJogadorInicialRound();

        for (Jogador jogador : partida.getJogadores()) {
            jogador.resetarParaNovoRound();
        }

        partida.setBaralho(baralhoService.criarBaralhoEmbaralhado());

        distribuirCartasIniciais(partida);

        definirJogadorInicialDoRound(partida);

        partida.setVencedorId(null);
        partida.setEmpateRound(false);
        partida.setStatus(StatusPartida.EM_ANDAMENTO);

        partidaRepository.salvar(partida);

        return new PartidaResponse(partida);
    }

    public void deletarPartida(UUID idPartida) {
        if (!partidaRepository.existePorId(idPartida)) {
            throw new RecursoNaoEncontradoException("Partida não encontrada.");
        }
        partidaRepository.deletar(idPartida);
    }

    private void definirJogadorInicialDoRound(Partida partida) {
        partida.setJogadorAtualId(partida.getIdJogadorInicialRound());
    }
}