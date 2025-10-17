package itb.grupo6.vencemed.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import itb.grupo6.vencemed.model.entity.Favorito;
import itb.grupo6.vencemed.model.entity.Usuario;
import itb.grupo6.vencemed.model.entity.Estabelecimento;
import itb.grupo6.vencemed.repository.FavoritoRepository;

@Service
public class FavoritoService {

    @Autowired
    private FavoritoRepository favoritoRepository;

    public Favorito favoritar(Usuario usuario, Estabelecimento estabelecimento) {
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

    public List<Favorito> listarPorUsuario(Usuario usuario) {
        return favoritoRepository.findByUsuario(usuario);
    }
    public void desfavoritar(Usuario usuario, Estabelecimento estabelecimento) {
        Favorito favorito = favoritoRepository.findByUsuarioAndEstabelecimento(usuario, estabelecimento)
                .orElseThrow(() -> new RuntimeException("Favorito não encontrado para este usuário e estabelecimento."));

        favoritoRepository.delete(favorito);
    }

}
