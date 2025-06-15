package br.com.unicuritiba.ProjectValidacaoBoleto.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.unicuritiba.ProjectValidacaoBoleto.models.Empresa;
import br.com.unicuritiba.ProjectValidacaoBoleto.repositories.EmpresaRepository;

@RestController
public class EmpresaControllers {
	@Autowired
	EmpresaRepository repository;
	
	@GetMapping("/Empresa")
	public ResponseEntity<Object> getEmpresa(){
		return ResponseEntity.ok(repository.findAll());	
	}
	@GetMapping("/Empresa/id")
	public ResponseEntity<Empresa> getEmpresa(@PathVariable long id) {
		return ResponseEntity.ok(repository.findById(id).get());
	}
	@GetMapping("/Empresa/cnpj")
	public ResponseEntity<Empresa> getEmpresacnpj(@PathVariable String cnpj) {
		return ResponseEntity.ok(repository.findByCNPJ(cnpj).get());
	}
	@GetMapping("/Empresa/nome")
	public ResponseEntity<Empresa> getBoletonome(@PathVariable String nome){
		return ResponseEntity.ok(repository.findByNome(nome).get());
	}	
	
@PostMapping("/Empresa")
public ResponseEntity<Empresa> SaveEmpresa(@RequestBody Empresa empresa){
	Empresa savedEmpresa = repository.save(empresa);
	return ResponseEntity.ok(savedEmpresa);
}	
@DeleteMapping("/Empresa{id}")
public void removieBoleto(@PathVariable long id) {
	repository.deleteById(id);
}
@PutMapping("/Empresa/{id}")
public ResponseEntity<Empresa> updateEmpresa(@PathVariable long id, @RequestBody Empresa empresa) {
	empresa.setId(id);
	Empresa savedEmpresa = repository.save(empresa);
	return ResponseEntity.ok(savedEmpresa);
}

	
}
