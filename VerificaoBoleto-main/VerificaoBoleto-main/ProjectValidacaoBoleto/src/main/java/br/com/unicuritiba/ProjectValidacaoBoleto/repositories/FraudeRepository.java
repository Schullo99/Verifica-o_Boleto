package br.com.unicuritiba.ProjectValidacaoBoleto.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.unicuritiba.ProjectValidacaoBoleto.models.Fraude;

public interface FraudeRepository extends JpaRepository<Fraude, Long> {
	Optional<Fraude> findByCPF(String CPF);
	Optional<Fraude> findByCodigoBarras(String codigoBarras);
	Optional<Fraude> findByNome(String nome);
}
