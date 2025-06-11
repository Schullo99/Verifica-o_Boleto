package br.com.unicuritiba.ProjectValidacaoBoleto.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.unicuritiba.ProjectValidacaoBoleto.models.Boleto;


public interface BoletoRepository 
	extends JpaRepository<Boleto, Long>{

	Optional<Boleto> findByCpf(String codigo_Cpf);
	Optional<Boleto> findByLinhaDigitavel(String linhaDigitavel);
	Optional<Boleto> findByNome(String nome);

}
