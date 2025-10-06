package itb.grupo6.vencemed.rest.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import itb.grupo6.vencemed.model.entity.Usuario;
import itb.grupo6.vencemed.service.UsuarioService;
import itb.grupo6.vencemed.rest.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/test")
    public String getTest() {
        return "Olá, Usuario!";
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Usuario> findById(@PathVariable long id) {
        Usuario usuario = usuarioService.findById(id);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        } else {
            throw new ResourceNotFoundException("Usuário não encontrado!");
        }
    }

    @GetMapping("/listar/{adminId}")
    public ResponseEntity<?> listarUsuarios(@PathVariable long adminId) {
        Optional<Usuario> admin = usuarioService.findByIdOptional(adminId);
        if (admin.isPresent() && "ADMIN".equalsIgnoreCase(admin.get().getNivelAcesso())) {
            List<Usuario> usuarios = usuarioService.findAll();
            if (usuarios.isEmpty()) {
                throw new ResourceNotFoundException("Nenhum usuário encontrado!");
            }
            return ResponseEntity.ok(usuarios);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Somente ADMIN pode listar usuários.");
    }

    @PostMapping("/salvar")
    public ResponseEntity<Usuario> salvar(@RequestBody Usuario usuario) {
        Usuario novoUsuario = usuarioService.save(usuario);
        if (novoUsuario != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // email já existente
        }
    }

    @DeleteMapping("/delete/{adminId}/{id}")
    public ResponseEntity<?> delete(@PathVariable long adminId, @PathVariable long id) {
        Optional<Usuario> admin = usuarioService.findByIdOptional(adminId);
        if (admin.isPresent() && "ADMIN".equalsIgnoreCase(admin.get().getNivelAcesso())) {
            boolean deletado = usuarioService.delete(id);
            if (deletado) {
                return ResponseEntity.noContent().build();
            } else {
                throw new ResourceNotFoundException("Usuário não encontrado!");
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Somente ADMIN pode excluir usuários.");
    }

    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestParam String email, @RequestParam String senha) {
        Usuario usuario = usuarioService.login(email, senha);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/alterarSenha/{id}")
    public ResponseEntity<Usuario> alterarSenha(@PathVariable long id, @RequestParam String novaSenha) {
        Usuario usuarioAtualizado = usuarioService.alterarSenha(id, novaSenha);
        if (usuarioAtualizado != null) {
            return ResponseEntity.ok(usuarioAtualizado);
        } else {
            throw new ResourceNotFoundException("Usuário não encontrado!");
        }
    }

    @PutMapping("/inativar/{adminId}/{id}")
    public ResponseEntity<?> inativar(@PathVariable long adminId, @PathVariable long id) {
        Optional<Usuario> admin = usuarioService.findByIdOptional(adminId);
        if (admin.isPresent() && "ADMIN".equalsIgnoreCase(admin.get().getNivelAcesso())) {
            Usuario usuarioAtualizado = usuarioService.inativar(id);
            if (usuarioAtualizado != null) {
                return ResponseEntity.ok(usuarioAtualizado);
            } else {
                throw new ResourceNotFoundException("Usuário não encontrado!");
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Somente ADMIN pode inativar usuários.");
    }

    @PutMapping("/reativar/{adminId}/{id}")
    public ResponseEntity<?> reativar(@PathVariable long adminId, @PathVariable long id) {
        Optional<Usuario> admin = usuarioService.findByIdOptional(adminId);
        if (admin.isPresent() && "ADMIN".equalsIgnoreCase(admin.get().getNivelAcesso())) {
            Usuario usuarioAtualizado = usuarioService.reativar(id);
            if (usuarioAtualizado != null) {
                return ResponseEntity.ok(usuarioAtualizado);
            } else {
                throw new ResourceNotFoundException("Usuário não encontrado!");
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Somente ADMIN pode reativar usuários.");
    }

    @PutMapping("/alterarNivel/{adminId}/{usuarioId}")
    public ResponseEntity<?> alterarNivel(
            @PathVariable long adminId,
            @PathVariable long usuarioId,
            @RequestParam String novoNivel) {

        Optional<Usuario> admin = usuarioService.findByIdOptional(adminId);
        if (admin.isPresent() && "ADMIN".equalsIgnoreCase(admin.get().getNivelAcesso())) {
            Usuario atualizado = usuarioService.alterarNivel(usuarioId, novoNivel);
            if (atualizado != null) {
                return ResponseEntity.ok(atualizado);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Somente ADMIN pode alterar nível de acesso.");
    }
}
