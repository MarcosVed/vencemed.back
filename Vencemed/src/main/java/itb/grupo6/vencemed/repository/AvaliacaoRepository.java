package itb.grupo6.vencemed.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import itb.grupo6.vencemed.model.entity.Avaliacao;
import itb.grupo6.vencemed.model.entity.Usuario;
import itb.grupo6.vencemed.model.entity.Estabelecimento;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    Optional<Avaliacao> findByUsuarioAndEstabelecimento(Usuario usuario, Estabelecimento estabelecimento);

    List<Avaliacao> findByEstabelecimento(Estabelecimento estabelecimento);

    List<Avaliacao> findByUsuario(Usuario usuario);
}
