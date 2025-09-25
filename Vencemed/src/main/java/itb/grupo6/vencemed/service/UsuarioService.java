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

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Transactional
    public Usuario save(Usuario usuario) {
        Optional<Usuario> existente = usuarioRepository.findByEmail(usuario.getEmail());

        if (existente.isEmpty()) {
            String senha = Base64.getEncoder().encodeToString(usuario.getSenha().getBytes());

            usuario.setSenha(senha);
            usuario.setDataCadastro(LocalDateTime.now());
            usuario.setStatusUsuario("ATIVO");

            return usuarioRepository.save(usuario);
        }
        return null;
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
    public Usuario login(String email, String senha) {
        Optional<Usuario> _usuario = usuarioRepository.findByEmail(email);

        if (_usuario.isPresent()) {
            Usuario usuario = _usuario.get();
            if ("ATIVO".equals(usuario.getStatusUsuario())) {
                byte[] decodedPass = Base64.getDecoder().decode(usuario.getSenha());
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
            String senha = Base64.getEncoder().encodeToString(novaSenha.getBytes());

            usuarioAtualizado.setSenha(senha);
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
            String senha = Base64.getEncoder().encodeToString("12345678".getBytes());

            usuarioAtualizado.setSenha(senha);
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
            String senha = Base64.getEncoder().encodeToString("12345678".getBytes());

            usuarioAtualizado.setSenha(senha);
            usuarioAtualizado.setDataCadastro(LocalDateTime.now());
            usuarioAtualizado.setStatusUsuario("ATIVO");

            return usuarioRepository.save(usuarioAtualizado);
        }
        return null;
    }
}
