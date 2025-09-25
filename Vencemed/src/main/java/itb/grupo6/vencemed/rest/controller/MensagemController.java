package itb.grupo6.vencemed.rest.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import itb.grupo6.vencemed.model.entity.Mensagem;
import itb.grupo6.vencemed.service.MensagemService;
 
@RestController

@RequestMapping("/mensagem")

public class MensagemController {
 
	private MensagemService mensagemService;

	// Source -> Generate Constructor using Fields...

	public MensagemController(MensagemService mensagemService) {

		super();

		this.mensagemService = mensagemService;

	}

	@GetMapping("/test")

	public String getTest() {

		return "Olá, Mensagem!";

	}

	@GetMapping("/findById/{id}")

	public ResponseEntity<Mensagem> findById(@PathVariable long id){

		Mensagem mensagem = mensagemService.findById(id);	

		return new ResponseEntity<Mensagem>(mensagem, HttpStatus.OK);	

	}

	@GetMapping("/findAll")

	public ResponseEntity<List<Mensagem>> findAll(){

		List<Mensagem> mensagens = mensagemService.findAll();

		return new ResponseEntity<List<Mensagem>>(mensagens, HttpStatus.OK);

	}

	@GetMapping("/findByEmail/{email}")

	public ResponseEntity<List<Mensagem>> findByEmail(@PathVariable String email){

		List<Mensagem> mensagens = mensagemService.findByEmail(email);

		return new ResponseEntity<List<Mensagem>>(mensagens, HttpStatus.OK);

	}

	@GetMapping("/findAllAtivos")

	public ResponseEntity<List<Mensagem>> findAllAtivos(){

		List<Mensagem> mensagens = mensagemService.findByStatus("ATIVO");

		return new ResponseEntity<List<Mensagem>>(mensagens, HttpStatus.OK);

	}

}
 
 
 
 
 
 