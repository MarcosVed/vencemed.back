package itb.grupo6.vencemed.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import itb.grupo6.vencemed.model.entity.Coleta;

@Repository
public interface ColetaRepository extends JpaRepository<Coleta, Long> {
    
    // Busca todas as coletas associadas a um usuário específico
    List<Coleta> findByUsuarioId(Long usuarioId);
}
