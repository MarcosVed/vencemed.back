package itb.grupo6.vencemed.rest.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import itb.grupo6.vencemed.model.entity.Mensagem;
import itb.grupo6.vencemed.service.MensagemService;

@RestController
@RequestMapping("/mensagem")
public class MensagemController {

    private MensagemService mensagemService;

    public MensagemController(MensagemService mensagemService) {
        super();
        this.mensagemService = mensagemService;
    }

    @GetMapping("/test")
    public String getTest() {
        return "Olá, Mensagem!";
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Mensagem> findById(@PathVariable long id) {
        Mensagem mensagem = mensagemService.findById(id);
        return new ResponseEntity<>(mensagem, HttpStatus.OK);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Mensagem>> findAll() {
        List<Mensagem> mensagens = mensagemService.findAll();
        return new ResponseEntity<>(mensagens, HttpStatus.OK);
    }

    @GetMapping("/findByEmail/{email}")
    public ResponseEntity<List<Mensagem>> findByEmail(@PathVariable String email) {
        List<Mensagem> mensagens = mensagemService.findByEmail(email);
        return new ResponseEntity<>(mensagens, HttpStatus.OK);
    }

    @GetMapping("/findAllAtivos")
    public ResponseEntity<List<Mensagem>> findAllAtivos() {
        List<Mensagem> mensagens = mensagemService.findByStatus("ATIVO");
        return new ResponseEntity<>(mensagens, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<Mensagem> save(@RequestBody Mensagem mensagem) {
        Mensagem novaMensagem = mensagemService.save(mensagem);
        return new ResponseEntity<>(novaMensagem, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        boolean deleted = mensagemService.delete(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
