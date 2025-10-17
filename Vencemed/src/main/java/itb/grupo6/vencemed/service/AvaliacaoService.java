package itb.grupo6.vencemed.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import itb.grupo6.vencemed.model.entity.Avaliacao;
import itb.grupo6.vencemed.model.entity.Usuario;
import itb.grupo6.vencemed.model.entity.Estabelecimento;
import itb.grupo6.vencemed.repository.AvaliacaoRepository;

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    public Avaliacao avaliar(Usuario usuario, Estabelecimento estabelecimento, int nota, String comentario) {
        Optional<Avaliacao> existente = avaliacaoRepository.findByUsuarioAndEstabelecimento(usuario, estabelecimento);
        if (existente.isPresent()) {
            throw new RuntimeException("Usuário já avaliou este estabelecimento.");
        }

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setUsuario(usuario);
        avaliacao.setEstabelecimento(estabelecimento);
        avaliacao.setNota(nota);
        avaliacao.setComentario(comentario);
        avaliacao.setDataAvaliacao(LocalDateTime.now());

        return avaliacaoRepository.save(avaliacao);
    }

    public List<Avaliacao> listarPorEstabelecimento(Estabelecimento estabelecimento) {
        return avaliacaoRepository.findByEstabelecimento(estabelecimento);
    }

    public List<Avaliacao> listarPorUsuario(Usuario usuario) {
        return avaliacaoRepository.findByUsuario(usuario);
    }
}
