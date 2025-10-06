package itb.grupo6.vencemed.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import itb.grupo6.vencemed.model.entity.Estabelecimento;
import itb.grupo6.vencemed.model.entity.Usuario;
import itb.grupo6.vencemed.service.EstabelecimentoService;
import itb.grupo6.vencemed.service.UsuarioService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/estabelecimento")
public class EstabelecimentoController {

    private final EstabelecimentoService estabelecimentoService;
    private final UsuarioService usuarioService;

    public EstabelecimentoController(EstabelecimentoService estabelecimentoService, UsuarioService usuarioService) {
        this.estabelecimentoService = estabelecimentoService;
        this.usuarioService = usuarioService;
    }

    // Cadastro de estabelecimento (somente FARMÁCIA)
    @PostMapping("/cadastrar/{usuarioId}")
    public ResponseEntity<?> cadastrar(@PathVariable Long usuarioId, @RequestBody Estabelecimento estabelecimento) {
        Optional<Estabelecimento> estab = estabelecimentoService.cadastrar(usuarioId, estabelecimento);
        return estab.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(403).body("Somente FARMÁCIA pode cadastrar estabelecimento."));
    }

    // Atualização de estabelecimento (somente ADMIN)
    @PutMapping("/atualizar/{usuarioId}/{estabId}")
    public ResponseEntity<?> atualizar(@PathVariable Long usuarioId, @PathVariable Long estabId,
                                       @RequestBody Estabelecimento novosDados) {
        Optional<Estabelecimento> estab = estabelecimentoService.atualizar(usuarioId, estabId, novosDados);
        return estab.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(403).body("Somente ADMIN pode atualizar estabelecimento."));
    }

    // Listagem de todos os estabelecimentos (somente ADMIN)
    @GetMapping("/listar/{usuarioId}")
    public ResponseEntity<?> listarTodos(@PathVariable Long usuarioId) {
        Optional<Usuario> usuarioOpt = usuarioService.findByIdOptional(usuarioId);
        if (usuarioOpt.isPresent() && "ADMIN".equalsIgnoreCase(usuarioOpt.get().getNivelAcesso())) {
            List<Estabelecimento> estabelecimentos = estabelecimentoService.listarTodos();
            return ResponseEntity.ok(estabelecimentos);
        }
        return ResponseEntity.status(403).body("Somente ADMIN pode listar todos os estabelecimentos.");
    }

    // Listagem dos estabelecimentos de um usuário (FARMÁCIA)
    @GetMapping("/listarUsuario/{usuarioId}")
    public ResponseEntity<?> listarPorUsuario(@PathVariable Long usuarioId) {
        Optional<Usuario> usuarioOpt = usuarioService.findByIdOptional(usuarioId);
        if (usuarioOpt.isPresent() && "FARMACIA".equalsIgnoreCase(usuarioOpt.get().getNivelAcesso())) {
            List<Estabelecimento> estabelecimentos = estabelecimentoService.listarPorUsuario(usuarioOpt.get());
            return ResponseEntity.ok(estabelecimentos);
        }
        return ResponseEntity.status(403).body("Somente FARMÁCIA pode listar seus estabelecimentos.");
    }
}
