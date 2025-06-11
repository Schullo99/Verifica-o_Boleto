package br.com.unicuritiba.ProjectValidacaoBoleto.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.unicuritiba.ProjectValidacaoBoleto.models.Fraude;
import br.com.unicuritiba.ProjectValidacaoBoleto.repositories.FraudeRepository;

@RestController
public class FraudeControllers {
FraudeRepository repository;
	
	@GetMapping("/fraude")
	public ResponseEntity<Object> getFraude(){
		return ResponseEntity.ok(repository.findAll());	
	}
	@GetMapping("/fraude/id")
	public ResponseEntity<Fraude> getFraude(@PathVariable long id) {
		return ResponseEntity.ok(repository.findById(id).get());
	}
	@GetMapping("/fraude/cpf")
	public ResponseEntity<Fraude> getFraudecpf(@PathVariable String cpf) {
		return ResponseEntity.ok(repository.findByCPF(cpf).get());
	}
	@GetMapping("/fraude/codigo_barras")
	public ResponseEntity<Fraude> getFraudebr(@PathVariable String codigo_barras) {
		return ResponseEntity.ok(repository.findByCodigoBarras(codigo_barras).get());
	}
	@GetMapping("/Fraude/nome")
	public ResponseEntity<Fraude> getFraudenome(@PathVariable String nome){
		return ResponseEntity.ok(repository.findByNome(nome).get());
			
	}	
	@PostMapping("/Fraude")
	public ResponseEntity<Fraude> SaveBoleto(@RequestBody Fraude fraude){
		Fraude savedFraude = repository.save(fraude);
		return ResponseEntity.ok(savedFraude);
	}	
	@DeleteMapping("/fraude{id}")
	public void removieFraude(@PathVariable long id) {
		repository.deleteById(id);
	}
	@PutMapping("/Fraude/{id}")
	public ResponseEntity<Fraude> updateFraude(@PathVariable long id, @RequestBody Fraude fraude) {
	fraude.setId(id);
		Fraude savedFraude = repository.save(fraude);
		return ResponseEntity.ok(savedFraude);
	}
}
