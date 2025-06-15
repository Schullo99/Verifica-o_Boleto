
package br.com.unicuritiba.ProjectValidacaoBoleto.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.unicuritiba.ProjectValidacaoBoleto.models.Cliente;
import br.com.unicuritiba.ProjectValidacaoBoleto.repositories.ClienteRepository;

@RestController
public class ClienteControllers {
	
	@Autowired
	ClienteRepository repository;
	
	
	@GetMapping("/clientes")
	public ResponseEntity<List<Cliente>> getCliente(){
		return ResponseEntity.ok(repository.findAll());
	}
	
	@GetMapping("/cliente/{id}")
	public ResponseEntity<Cliente> getCliente(@PathVariable long id){
		return ResponseEntity.ok(repository.findById(id).get());
	}
	
	@PostMapping("/clientes")
	public ResponseEntity<Cliente> saveUser(
			@RequestBody Cliente cliente){
		Cliente savedCliente = repository.save(cliente);
		return ResponseEntity.ok(savedCliente);
	}
	
	@DeleteMapping("/clientes/{id}")
	public void removeCliente(@PathVariable long id) {
		 repository.deleteById(id);
	}
	
	@PutMapping("/clientes/{id}")
	public ResponseEntity<Cliente> updateCliente(@PathVariable long id,
			@RequestBody Cliente cliente) {
		cliente.setId(id);
		Cliente savedCliente = repository.save(cliente);
		return ResponseEntity.ok(savedCliente);
	}
	

}
