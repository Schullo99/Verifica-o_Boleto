package br.com.unicuritiba.ProjectValidacaoBoleto.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.unicuritiba.ProjectValidacaoBoleto.models.Empresa;

public interface EmpresaRepository 
	extends JpaRepository<Empresa, Long>{
	Optional<Empresa> findByCNPJ(String Cnpj);
	Optional<Empresa> findByNome(String nome);
}
