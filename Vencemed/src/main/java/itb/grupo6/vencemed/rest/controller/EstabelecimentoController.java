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

    // FARMÁCIA pode cadastrar
    @PostMapping("/cadastrar/{usuarioId}")
    public ResponseEntity<?> cadastrar(@PathVariable Long usuarioId, @RequestBody Estabelecimento estabelecimento) {
        Optional<Estabelecimento> estab = estabelecimentoService.cadastrar(usuarioId, estabelecimento);
        return estab.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(403).body("Somente FARMÁCIA pode cadastrar estabelecimento."));
    }

    // ADMIN e FARMÁCIA podem atualizar
    @PutMapping("/atualizar/{usuarioId}/{estabId}")
    public ResponseEntity<?> atualizar(@PathVariable Long usuarioId, @PathVariable Long estabId,
                                       @RequestBody Estabelecimento novosDados) {
        Optional<Usuario> usuarioOpt = usuarioService.findByIdOptional(usuarioId);
        if (usuarioOpt.isPresent()) {
            String nivel = usuarioOpt.get().getNivelAcesso();
            if ("ADMIN".equalsIgnoreCase(nivel) || "FARMACIA".equalsIgnoreCase(nivel)) {
                Optional<Estabelecimento> estab = estabelecimentoService.atualizar(usuarioId, estabId, novosDados);
                return estab.<ResponseEntity<?>>map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.status(404).body("Estabelecimento não encontrado."));
            }
        }
        return ResponseEntity.status(403).body("Somente ADMIN ou FARMÁCIA podem atualizar estabelecimento.");
    }

    // ADMIN pode listar todos
    @GetMapping("/listar/{usuarioId}")
    public ResponseEntity<?> listarTodos(@PathVariable Long usuarioId) {
        Optional<Usuario> usuarioOpt = usuarioService.findByIdOptional(usuarioId);
        if (usuarioOpt.isPresent() && "ADMIN".equalsIgnoreCase(usuarioOpt.get().getNivelAcesso())) {
            List<Estabelecimento> estabelecimentos = estabelecimentoService.listarTodos();
            return ResponseEntity.ok(estabelecimentos);
        }
        return ResponseEntity.status(403).body("Somente ADMIN pode listar todos os estabelecimentos.");
    }

    // FARMÁCIA pode listar seus próprios estabelecimentos
    @GetMapping("/listarUsuario/{usuarioId}")
    public ResponseEntity<?> listarPorUsuario(@PathVariable Long usuarioId) {
        Optional<Usuario> usuarioOpt = usuarioService.findByIdOptional(usuarioId);
        if (usuarioOpt.isPresent() && "FARMACIA".equalsIgnoreCase(usuarioOpt.get().getNivelAcesso())) {
            List<Estabelecimento> estabelecimentos = estabelecimentoService.listarPorUsuario(usuarioOpt.get());
            return ResponseEntity.ok(estabelecimentos);
        }
        return ResponseEntity.status(403).body("Somente FARMÁCIA pode listar seus estabelecimentos.");
    }

    // Qualquer usuário pode listar por CEP
    @GetMapping("/listarPorCep/{cep}")
    public ResponseEntity<?> listarPorCep(@PathVariable String cep) {
        List<Estabelecimento> estabelecimentos = estabelecimentoService.listarPorCep(cep);
        return ResponseEntity.ok(estabelecimentos);
    }

    // Novo endpoint público para agendamento de coleta
    @GetMapping("/publico")
    public ResponseEntity<?> listarPublico() {
        List<Estabelecimento> estabelecimentos = estabelecimentoService.listarTodos();
        return ResponseEntity.ok(estabelecimentos);
    }

    // ADMIN pode excluir
    @DeleteMapping("/excluir/{usuarioId}/{estabId}")
    public ResponseEntity<?> excluir(@PathVariable Long usuarioId, @PathVariable Long estabId) {
        Optional<Usuario> usuarioOpt = usuarioService.findByIdOptional(usuarioId);
        if (usuarioOpt.isPresent() && "ADMIN".equalsIgnoreCase(usuarioOpt.get().getNivelAcesso())) {
            boolean removido = estabelecimentoService.excluir(estabId);
            if (removido) {
                return ResponseEntity.ok("Estabelecimento excluído com sucesso.");
            } else {
                return ResponseEntity.status(404).body("Estabelecimento não encontrado.");
            }
        }
        return ResponseEntity.status(403).body("Somente ADMIN pode excluir estabelecimento.");
    }
}
