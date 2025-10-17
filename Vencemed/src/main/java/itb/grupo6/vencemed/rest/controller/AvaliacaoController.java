package itb.grupo6.vencemed.rest.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import itb.grupo6.vencemed.model.entity.Avaliacao;
import itb.grupo6.vencemed.model.entity.Usuario;
import itb.grupo6.vencemed.model.entity.Estabelecimento;
import itb.grupo6.vencemed.repository.AvaliacaoRepository;
import itb.grupo6.vencemed.repository.UsuarioRepository;
import itb.grupo6.vencemed.repository.EstabelecimentoRepository;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EstabelecimentoRepository estabelecimentoRepository;

    @PostMapping
    public Avaliacao criarAvaliacao(@RequestParam Long usuarioId,
                                    @RequestParam Long estabelecimentoId,
                                    @RequestParam int nota,
                                    @RequestParam(required = false) String comentario) {

        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow();
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(estabelecimentoId).orElseThrow();

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

    @GetMapping("/estabelecimento/{id}")
    public List<Avaliacao> listarPorEstabelecimento(@PathVariable Long id) {
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(id).orElseThrow();
        return avaliacaoRepository.findByEstabelecimento(estabelecimento);
    }

    @GetMapping("/usuario/{id}")
    public List<Avaliacao> listarPorUsuario(@PathVariable Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow();
        return avaliacaoRepository.findByUsuario(usuario);
    }
    @PutMapping("/{id}")
    public Avaliacao atualizarAvaliacao(@PathVariable Long id,
                                        @RequestParam int nota,
                                        @RequestParam(required = false) String comentario) {
        Avaliacao avaliacao = avaliacaoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Avaliação não encontrada."));

        avaliacao.setNota(nota);
        avaliacao.setComentario(comentario);
        avaliacao.setDataAvaliacao(LocalDateTime.now());

        return avaliacaoRepository.save(avaliacao);
    }



}
