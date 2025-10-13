package itb.grupo6.vencemed.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import itb.grupo6.vencemed.model.entity.Estabelecimento;
import itb.grupo6.vencemed.model.entity.Usuario;
import itb.grupo6.vencemed.repository.EstabelecimentoRepository;
import itb.grupo6.vencemed.repository.UsuarioRepository;

@Service
public class EstabelecimentoService {

    private final EstabelecimentoRepository estabelecimentoRepository;
    private final UsuarioRepository usuarioRepository;

    public EstabelecimentoService(EstabelecimentoRepository estabelecimentoRepository,
                                  UsuarioRepository usuarioRepository) {
        this.estabelecimentoRepository = estabelecimentoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // FARMÁCIA pode cadastrar
    public Optional<Estabelecimento> cadastrar(Long usuarioId, Estabelecimento estabelecimento) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);

        if (usuarioOpt.isPresent() && "FARMACIA".equalsIgnoreCase(usuarioOpt.get().getNivelAcesso())) {
            estabelecimento.setUsuario(usuarioOpt.get());
            estabelecimento.setDataCadastro(LocalDateTime.now());
            estabelecimento.setStatusEstabelecimento("ATIVO");
            return Optional.of(estabelecimentoRepository.save(estabelecimento));
        }
        return Optional.empty();
    }

    // ADMIN e FARMÁCIA podem atualizar
    public Optional<Estabelecimento> atualizar(Long usuarioId, Long estabId, Estabelecimento novosDados) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        Optional<Estabelecimento> estabOpt = estabelecimentoRepository.findById(estabId);

        if (usuarioOpt.isPresent() && estabOpt.isPresent()) {
            String nivel = usuarioOpt.get().getNivelAcesso();
            if ("ADMIN".equalsIgnoreCase(nivel) || "FARMACIA".equalsIgnoreCase(nivel)) {
                Estabelecimento estab = estabOpt.get();
                estab.setNome(novosDados.getNome());
                estab.setInfo(novosDados.getInfo());
                estab.setCep(novosDados.getCep());
                estab.setNumero(novosDados.getNumero());
                estab.setComplemento(novosDados.getComplemento());
                estab.setTelefone(novosDados.getTelefone());
                estab.setTipo(novosDados.getTipo());
                estab.setColeta(novosDados.getColeta());
                estab.setStatusEstabelecimento(novosDados.getStatusEstabelecimento());

                return Optional.of(estabelecimentoRepository.save(estab));
            }
        }
        return Optional.empty();
    }

    // ADMIN pode excluir
    public boolean excluir(Long estabId) {
        Optional<Estabelecimento> estabOpt = estabelecimentoRepository.findById(estabId);
        if (estabOpt.isPresent()) {
            estabelecimentoRepository.deleteById(estabId);
            return true;
        }
        return false;
    }

    // Listagem geral
    public List<Estabelecimento> listarTodos() {
        return estabelecimentoRepository.findAll();
    }

    // Listagem por usuário (FARMÁCIA)
    public List<Estabelecimento> listarPorUsuario(Usuario usuario) {
        return estabelecimentoRepository.findByUsuario(usuario);
    }

    // Listagem por CEP (público)
    public List<Estabelecimento> listarPorCep(String cep) {
        return estabelecimentoRepository.findByCep(cep);
    }
}