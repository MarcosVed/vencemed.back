package itb.grupo6.vencemed.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    // Solicitação de cadastro por USER
    public Optional<Estabelecimento> solicitarCadastro(Long usuarioId, Estabelecimento estabelecimento) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);

        if (usuarioOpt.isPresent() && "USER".equalsIgnoreCase(usuarioOpt.get().getNivelAcesso())) {
            estabelecimento.setUsuario(usuarioOpt.get());
            estabelecimento.setDataCadastro(LocalDateTime.now());
            estabelecimento.setStatusEstabelecimento("PENDENTE");
            return Optional.of(estabelecimentoRepository.save(estabelecimento));
        }
        return Optional.empty();
    }

    // ADMIN revisa solicitação e aprova ou rejeita
    public Optional<Estabelecimento> revisarSolicitacao(Long adminId, Long estabId, boolean aprovar) {
        Optional<Usuario> adminOpt = usuarioRepository.findById(adminId);
        Optional<Estabelecimento> estabOpt = estabelecimentoRepository.findById(estabId);

        if (adminOpt.isPresent() && "ADMIN".equalsIgnoreCase(adminOpt.get().getNivelAcesso()) && estabOpt.isPresent()) {
            Estabelecimento estab = estabOpt.get();
            Usuario usuario = estab.getUsuario();

            if (aprovar) {
                estab.setStatusEstabelecimento("ATIVO");
                usuario.setNivelAcesso("FARMACIA");
                usuarioRepository.save(usuario);
            } else {
                estab.setStatusEstabelecimento("INATIVO");
            }

            return Optional.of(estabelecimentoRepository.save(estab));
        }
        return Optional.empty();
    }

    // FARMÁCIA pode alterar seu próprio estabelecimento
    public Optional<Estabelecimento> atualizar(Long usuarioId, Long estabId, Estabelecimento novosDados) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        Optional<Estabelecimento> estabOpt = estabelecimentoRepository.findById(estabId);

        if (usuarioOpt.isPresent() && estabOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            Estabelecimento estab = estabOpt.get();

            boolean isAdmin = "ADMIN".equalsIgnoreCase(usuario.getNivelAcesso());
            boolean isFarmaciaDona = "FARMACIA".equalsIgnoreCase(usuario.getNivelAcesso()) &&
                                      Objects.equals(estab.getUsuario().getId(), usuarioId);

            if (isAdmin || isFarmaciaDona) {
                estab.setNome(novosDados.getNome());
                estab.setInfo(novosDados.getInfo());
                estab.setCep(novosDados.getCep());
                estab.setNumero(novosDados.getNumero());
                estab.setComplemento(novosDados.getComplemento());
                estab.setTelefone(novosDados.getTelefone());
                estab.setTipo(novosDados.getTipo());
                estab.setColeta(novosDados.getColeta());
                estab.setStatusEstabelecimento(novosDados.getStatusEstabelecimento());
                estab.setFotoEst(novosDados.getFotoEst()); // inclusão do campo binário

                return Optional.of(estabelecimentoRepository.save(estab));
            }
        }
        return Optional.empty();
    }

    // FARMÁCIA pode excluir seu próprio estabelecimento
    public boolean excluir(Long usuarioId, Long estabId) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        Optional<Estabelecimento> estabOpt = estabelecimentoRepository.findById(estabId);

        if (usuarioOpt.isPresent() && estabOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            Estabelecimento estab = estabOpt.get();

            boolean isAdmin = "ADMIN".equalsIgnoreCase(usuario.getNivelAcesso());
            boolean isFarmaciaDona = "FARMACIA".equalsIgnoreCase(usuario.getNivelAcesso()) &&
                                      Objects.equals(estab.getUsuario().getId(), usuarioId);

            if (isAdmin || isFarmaciaDona) {
                estabelecimentoRepository.deleteById(estabId);
                return true;
            }
        }
        return false;
    }
 
    // FARMÁCIA pode cadastrar diretamente
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
    // Listagem geral (ADMIN)
    public List<Estabelecimento> listarTodos() {
        return estabelecimentoRepository.findAll();
    }

    // Listagem de solicitações pendentes (ADMIN)
    public List<Estabelecimento> listarPendentes() {
        return estabelecimentoRepository.findByStatusEstabelecimento("PENDENTE");
    }

    // Listagem por usuário (FARMÁCIA)
    public List<Estabelecimento> listarPorUsuario(Usuario usuario) {
        return estabelecimentoRepository.findByUsuario(usuario);
    }

    // Listagem por CEP (público)
    public List<Estabelecimento> listarPorCep(String cep) {
        return estabelecimentoRepository.findByCep(cep);
    }
    public Estabelecimento atualizarImagem(Long id, MultipartFile imagem) throws IOException {
        Optional<Estabelecimento> optional = estabelecimentoRepository.findById(id);
        if (optional.isPresent()) {
            Estabelecimento est = optional.get();
            est.setFotoEst(imagem.getBytes()); // Campo byte[] no modelo
            return estabelecimentoRepository.save(est);
        } else {
            throw new RuntimeException("Estabelecimento não encontrado com ID: " + id);
        }
    }

    public Optional<Estabelecimento> findById(Long id) {
        return estabelecimentoRepository.findById(id);
    }
}
