package itb.grupo6.vencemed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import itb.grupo6.vencemed.model.entity.Usuario;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Buscar usuário por email
    Optional<Usuario> findByEmail(String email);

    // Buscar usuário por nome (lista, já que pode ter repetidos)
    List<Usuario> findByNomeContainingIgnoreCase(String nome);

    // Buscar todos os usuários por status (ATIVO, INATIVO, TROCAR_SENHA)
    List<Usuario> findByStatusUsuario(String statusUsuario);

    // Buscar todos os usuários por nível de acesso (ADMIN, FARMACIA, USER)
    List<Usuario> findByNivelAcesso(String nivelAcesso);

    // Buscar usuário por email e status (ex: login apenas se ATIVO)
    Optional<Usuario> findByEmailAndStatusUsuario(String email, String statusUsuario);
}
