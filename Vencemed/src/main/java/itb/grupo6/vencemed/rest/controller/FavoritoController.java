package itb.grupo6.vencemed.rest.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import itb.grupo6.vencemed.model.entity.Favorito;
import itb.grupo6.vencemed.model.entity.Usuario;
import itb.grupo6.vencemed.model.entity.Estabelecimento;
import itb.grupo6.vencemed.repository.FavoritoRepository;
import itb.grupo6.vencemed.repository.UsuarioRepository;
import itb.grupo6.vencemed.repository.EstabelecimentoRepository;

@RestController
@RequestMapping("/favoritos")
public class FavoritoController {

    @Autowired
    private FavoritoRepository favoritoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EstabelecimentoRepository estabelecimentoRepository;

    @PostMapping
    public Favorito adicionarFavorito(@RequestParam Long usuarioId,
                                      @RequestParam Long estabelecimentoId) {

        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow();
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(estabelecimentoId).orElseThrow();

        Optional<Favorito> existente = favoritoRepository.findByUsuarioAndEstabelecimento(usuario, estabelecimento);
        if (existente.isPresent()) {
            throw new RuntimeException("Estabelecimento já está favoritado por este usuário.");
        }

        Favorito favorito = new Favorito();
        favorito.setUsuario(usuario);
        favorito.setEstabelecimento(estabelecimento);
        favorito.setDataFavoritado(LocalDateTime.now());

        return favoritoRepository.save(favorito);
    }

    @GetMapping("/usuario/{id}")
    public List<Favorito> listarFavoritosPorUsuario(@PathVariable Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow();
        return favoritoRepository.findByUsuario(usuario);
    }
    
    @DeleteMapping
    public void removerFavorito(@RequestParam Long usuarioId,
                                @RequestParam Long estabelecimentoId) {

        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow();
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(estabelecimentoId).orElseThrow();

        Favorito favorito = favoritoRepository.findByUsuarioAndEstabelecimento(usuario, estabelecimento)
                .orElseThrow(() -> new RuntimeException("Favorito não encontrado para este usuário e estabelecimento."));

        favoritoRepository.delete(favorito);
    }

}

