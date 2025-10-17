package itb.grupo6.vencemed.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import itb.grupo6.vencemed.model.entity.Favorito;
import itb.grupo6.vencemed.model.entity.Usuario;
import itb.grupo6.vencemed.model.entity.Estabelecimento;

@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Long> {

    Optional<Favorito> findByUsuarioAndEstabelecimento(Usuario usuario, Estabelecimento estabelecimento);

    List<Favorito> findByUsuario(Usuario usuario);
}
