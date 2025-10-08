package itb.grupo6.vencemed.service;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import itb.grupo6.vencemed.model.entity.Usuario;
import itb.grupo6.vencemed.repository.UsuarioRepository;
import jakarta.transaction.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario findById(long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Optional<Usuario> findByIdOptional(long id) {
        return usuarioRepository.findById(id);
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Transactional
    public Usuario save(Usuario usuario) {
        Optional<Usuario> existente = usuarioRepository.findByEmail(usuario.getEmail());
        if (existente.isEmpty()) {
            // Codificar a senha em Base64
            String senhaCodificada = Base64.getEncoder().encodeToString(usuario.getSenha().getBytes());
            usuario.setSenha(senhaCodificada);

            usuario.setDataCadastro(LocalDateTime.now());
            usuario.setStatusUsuario("ATIVO");

            if (usuario.getNivelAcesso() == null) {
                usuario.setNivelAcesso("USER");
            }

            return usuarioRepository.save(usuario);
        }
        return null; // email já existe
    }

    @Transactional
    public boolean delete(long id) {
        Usuario usuario = findById(id);
        if (usuario != null) {
            usuarioRepository.delete(usuario);
            return true;
        }
        return false;
    }

    @Transactional
    public Optional<Usuario> login(String email, String senha) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

        if (usuario.isPresent()) {
            if (!usuario.get().getStatusUsuario().equals("INATIVO")) {
                byte[] decodedPass = Base64.getDecoder().decode(usuario.get().getSenha());
                if (new String(decodedPass).equals(senha)) {
                    return usuario;
                }
            }
        }
        return null;
    }

    @Transactional
    public Usuario alterarSenha(long id, String novaSenha) {
        Optional<Usuario> _usuario = usuarioRepository.findById(id);
        if (_usuario.isPresent()) {
            Usuario usuarioAtualizado = _usuario.get();

            // Codificar nova senha em Base64
            String senhaCodificada = Base64.getEncoder().encodeToString(novaSenha.getBytes());
            usuarioAtualizado.setSenha(senhaCodificada);

            usuarioAtualizado.setDataCadastro(LocalDateTime.now());
            usuarioAtualizado.setStatusUsuario("ATIVO");

            return usuarioRepository.save(usuarioAtualizado);
        }
        return null;
    }

    @Transactional
    public Usuario inativar(long id) {
        Optional<Usuario> _usuario = usuarioRepository.findById(id);
        if (_usuario.isPresent()) {
            Usuario usuarioAtualizado = _usuario.get();
            usuarioAtualizado.setSenha("12345678");
            usuarioAtualizado.setDataCadastro(LocalDateTime.now());
            usuarioAtualizado.setStatusUsuario("INATIVO");
            return usuarioRepository.save(usuarioAtualizado);
        }
        return null;
    }

    @Transactional
    public Usuario reativar(long id) {
        Optional<Usuario> _usuario = usuarioRepository.findById(id);
        if (_usuario.isPresent()) {
            Usuario usuarioAtualizado = _usuario.get();
            usuarioAtualizado.setSenha("12345678");
            usuarioAtualizado.setDataCadastro(LocalDateTime.now());
            usuarioAtualizado.setStatusUsuario("ATIVO");
            return usuarioRepository.save(usuarioAtualizado);
        }
        return null;
    }

    @Transactional
    public Usuario alterarNivel(long usuarioId, String novoNivel) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setNivelAcesso(novoNivel.toUpperCase());
            return usuarioRepository.save(usuario);
        }
        return null;
    }
}
