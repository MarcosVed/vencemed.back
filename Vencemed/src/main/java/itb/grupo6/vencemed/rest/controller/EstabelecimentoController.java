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

    // USER solicita cadastro de estabelecimento
    @PostMapping("/solicitar/{usuarioId}")
    public ResponseEntity<?> solicitarCadastro(@PathVariable Long usuarioId, @RequestBody Estabelecimento estabelecimento) {
        Optional<Estabelecimento> estab = estabelecimentoService.solicitarCadastro(usuarioId, estabelecimento);
        return estab.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(403).body("Somente usuários comuns podem solicitar cadastro."));
    }

    // ADMIN revisa solicitação (aprova ou recusa)
    @PutMapping("/revisar/{adminId}/{estabId}")
    public ResponseEntity<?> revisarSolicitacao(@PathVariable Long adminId, @PathVariable Long estabId,
                                                @RequestParam boolean aprovar) {
        Optional<Estabelecimento> estab = estabelecimentoService.revisarSolicitacao(adminId, estabId, aprovar);
        return estab.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(403).body("Falha ao revisar solicitação."));
    }

    // ADMIN lista solicitações pendentes
    @GetMapping("/pendentes/{adminId}")
    public ResponseEntity<?> listarPendentes(@PathVariable Long adminId) {
        Optional<Usuario> usuarioOpt = usuarioService.findByIdOptional(adminId);
        if (usuarioOpt.isPresent() && "ADMIN".equalsIgnoreCase(usuarioOpt.get().getNivelAcesso())) {
            List<Estabelecimento> pendentes = estabelecimentoService.listarPendentes();
            return ResponseEntity.ok(pendentes);
        }
        return ResponseEntity.status(403).body("Somente ADMIN pode visualizar solicitações pendentes.");
    }

    // FARMÁCIA pode cadastrar diretamente
    @PostMapping("/cadastrar/{usuarioId}")
    public ResponseEntity<?> cadastrar(@PathVariable Long usuarioId, @RequestBody Estabelecimento estabelecimento) {
        Optional<Estabelecimento> estab = estabelecimentoService.cadastrar(usuarioId, estabelecimento);
        return estab.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(403).body("Somente FARMÁCIA pode cadastrar estabelecimento."));
    }

    // ADMIN ou FARMÁCIA (dona) podem atualizar
    @PutMapping("/atualizar/{usuarioId}/{estabId}")
    public ResponseEntity<?> atualizar(@PathVariable Long usuarioId, @PathVariable Long estabId,
                                       @RequestBody Estabelecimento novosDados) {
        Optional<Estabelecimento> estab = estabelecimentoService.atualizar(usuarioId, estabId, novosDados);
        return estab.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(403).body("Usuário não autorizado ou estabelecimento não encontrado."));
    }

    // ADMIN ou FARMÁCIA (dona) podem excluir
    @DeleteMapping("/excluir/{usuarioId}/{estabId}")
    public ResponseEntity<?> excluir(@PathVariable Long usuarioId, @PathVariable Long estabId) {
        boolean removido = estabelecimentoService.excluir(usuarioId, estabId);
        if (removido) {
            return ResponseEntity.ok("Estabelecimento excluído com sucesso.");
        } else {
            return ResponseEntity.status(403).body("Usuário não autorizado ou estabelecimento não encontrado.");
        }
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

    // Público geral pode ver todos os estabelecimentos ativos
    @GetMapping("/publico")
    public ResponseEntity<?> listarPublico() {
        List<Estabelecimento> estabelecimentos = estabelecimentoService.listarTodos();
        return ResponseEntity.ok(estabelecimentos);
    }
}
