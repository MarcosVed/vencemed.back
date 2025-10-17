package itb.grupo6.vencemed.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import itb.grupo6.vencemed.model.entity.Coleta;
import itb.grupo6.vencemed.service.ColetaService;

@RestController
@RequestMapping("/coletas")
@CrossOrigin(origins = "*") // Permite acesso do Flutter
public class ColetaController {

    @Autowired
    private ColetaService coletaService;

    // Criar / Agendar coleta
    @PostMapping("/agendar")
    public ResponseEntity<Coleta> agendarColeta(@RequestBody Coleta coleta) {
        Coleta novaColeta = coletaService.agendarColeta(coleta);
        return ResponseEntity.ok(novaColeta);
    }

    // Listar todas
    @GetMapping
    public ResponseEntity<List<Coleta>> listarTodas() {
        return ResponseEntity.ok(coletaService.listarTodas());
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Coleta> buscarPorId(@PathVariable Long id) {
        return coletaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Listar coletas por estabelecimento
    @GetMapping("/estabelecimento/{estabelecimentoId}")
    public ResponseEntity<List<Coleta>> listarPorEstabelecimento(@PathVariable Long estabelecimentoId) {
        List<Coleta> coletas = coletaService.listarPorEstabelecimento(estabelecimentoId);
        return ResponseEntity.ok(coletas);
    }

    // Listar coletas do usuário
    @GetMapping("/minhas")
    public ResponseEntity<List<Coleta>> listarMinhas(@RequestParam Long usuarioId) {
        return ResponseEntity.ok(coletaService.listarPorUsuario(usuarioId));
    }

    // Atualizar coleta
    @PutMapping("/{id}")
    public ResponseEntity<Coleta> atualizarColeta(@PathVariable Long id, @RequestBody Coleta coleta) {
        try {
            Coleta atualizada = coletaService.atualizarColeta(id, coleta);
            return ResponseEntity.ok(atualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Deletar coleta
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarColeta(@PathVariable Long id) {
        coletaService.deletarColeta(id);
        return ResponseEntity.noContent().build();
    }
}
