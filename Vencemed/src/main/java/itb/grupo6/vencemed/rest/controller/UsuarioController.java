package itb.grupo6.vencemed.rest.controller;

import java.util.List;

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

    @GetMapping("/listar")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.findAll();
        if (usuarios.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum usuário encontrado!");
        }
        return ResponseEntity.ok(usuarios);
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        boolean deletado = usuarioService.delete(id);
        if (deletado) {
            return ResponseEntity.noContent().build();
        } else {
            throw new ResourceNotFoundException("Usuário não encontrado!");
        }
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

    @PutMapping("/inativar/{id}")
    public ResponseEntity<Usuario> inativar(@PathVariable long id) {
        Usuario usuarioAtualizado = usuarioService.inativar(id);
        if (usuarioAtualizado != null) {
            return ResponseEntity.ok(usuarioAtualizado);
        } else {
            throw new ResourceNotFoundException("Usuário não encontrado!");
        }
    }

    @PutMapping("/reativar/{id}")
    public ResponseEntity<Usuario> reativar(@PathVariable long id) {
        Usuario usuarioAtualizado = usuarioService.reativar(id);
        if (usuarioAtualizado != null) {
            return ResponseEntity.ok(usuarioAtualizado);
        } else {
            throw new ResourceNotFoundException("Usuário não encontrado!");
        }
    }
}




