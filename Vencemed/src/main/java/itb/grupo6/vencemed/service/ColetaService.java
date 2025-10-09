package itb.grupo6.vencemed.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import itb.grupo6.vencemed.model.entity.Coleta;
import itb.grupo6.vencemed.repository.ColetaRepository;

@Service
public class ColetaService {

    @Autowired
    private ColetaRepository coletaRepository;

    // Criar / agendar coleta
    public Coleta agendarColeta(Coleta coleta) {
        coleta.setStatusColeta("ATIVO");
        return coletaRepository.save(coleta);
    }

    // Buscar todas
    public List<Coleta> listarTodas() {
        return coletaRepository.findAll();
    }

    // Buscar por ID
    public Optional<Coleta> buscarPorId(Long id) {
        return coletaRepository.findById(id);
    }

    // Buscar coletas por usuário
    public List<Coleta> listarPorUsuario(Long usuarioId) {
        return coletaRepository.findByUsuarioId(usuarioId);
    }

    // Atualizar coleta
    public Coleta atualizarColeta(Long id, Coleta coletaAtualizada) {
        return coletaRepository.findById(id).map(c -> {
            c.setInfo(coletaAtualizada.getInfo());
            c.setCep(coletaAtualizada.getCep());
            c.setNumero(coletaAtualizada.getNumero());
            c.setComplemento(coletaAtualizada.getComplemento());
            c.setTelefone(coletaAtualizada.getTelefone());
            c.setTipoMedicamento(coletaAtualizada.getTipoMedicamento());
            c.setTipoColeta(coletaAtualizada.getTipoColeta());
            c.setDataColeta(coletaAtualizada.getDataColeta());
            c.setUsuario(coletaAtualizada.getUsuario());
            c.setEstabelecimento(coletaAtualizada.getEstabelecimento());
            c.setStatusColeta(coletaAtualizada.getStatusColeta());
            return coletaRepository.save(c);
        }).orElseThrow(() -> new RuntimeException("Coleta não encontrada com ID: " + id));
    }

    // Deletar
    public void deletarColeta(Long id) {
        coletaRepository.deleteById(id);
    }
}
