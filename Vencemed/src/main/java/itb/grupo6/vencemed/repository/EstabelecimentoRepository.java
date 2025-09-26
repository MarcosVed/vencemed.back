package itb.grupo6.vencemed.repository;

import itb.grupo6.vencemed.model.entity.Estabelecimento;
import itb.grupo6.vencemed.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstabelecimentoRepository extends JpaRepository<Estabelecimento, Long> {

    // Buscar todos os estabelecimentos de um usuário específico
    List<Estabelecimento> findByUsuario(Usuario usuario);

    // Buscar estabelecimentos pelo nome (contendo parte do nome, ignorando maiúsculas/minúsculas)
    List<Estabelecimento> findByNomeContainingIgnoreCase(String nome);

    // Buscar estabelecimentos por status
    List<Estabelecimento> findByStatusEstabelecimento(String status);

    // Verificar se já existe um estabelecimento vinculado a um usuário
    boolean existsByUsuario(Usuario usuario);
}
