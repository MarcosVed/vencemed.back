package itb.grupo6.vencemed.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import itb.grupo6.vencemed.model.entity.Estabelecimento;
import itb.grupo6.vencemed.service.EstabelecimentoService;

import java.util.Optional;

@RestController
@RequestMapping("/estabelecimento")
public class EstabelecimentoController {

    private final EstabelecimentoService estabelecimentoService;

    public EstabelecimentoController(EstabelecimentoService estabelecimentoService) {
        this.estabelecimentoService = estabelecimentoService;
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
}
