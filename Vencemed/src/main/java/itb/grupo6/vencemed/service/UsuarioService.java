package itb.grupo6.vencemed.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import itb.grupo6.vencemed.model.entity.Usuario;
import itb.grupo6.vencemed.repository.UsuarioRepository;
import jakarta.transaction.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha())); // senha com hash seguro
            usuario.setDataCadastro(LocalDateTime.now());
            usuario.setStatusUsuario("ATIVO");

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
    public Usuario login(String email, String senha) {
        Optional<Usuario> _usuario = usuarioRepository.findByEmailAndStatusUsuario(email, "ATIVO");

        if (_usuario.isPresent()) {
            Usuario usuario = _usuario.get();
            if (passwordEncoder.matches(senha, usuario.getSenha())) {
                return usuario;
            }
        }
        return null;
    }

    @Transactional
    public Usuario alterarSenha(long id, String novaSenha) {
        Optional<Usuario> _usuario = usuarioRepository.findById(id);

        if (_usuario.isPresent()) {
            Usuario usuarioAtualizado = _usuario.get();
            usuarioAtualizado.setSenha(passwordEncoder.encode(novaSenha));
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
            usuarioAtualizado.setSenha(passwordEncoder.encode("12345678"));
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
            usuarioAtualizado.setSenha(passwordEncoder.encode("12345678"));
            usuarioAtualizado.setDataCadastro(LocalDateTime.now());
            usuarioAtualizado.setStatusUsuario("ATIVO");

            return usuarioRepository.save(usuarioAtualizado);
        }
        return null;
    }
}
