package br.com.unicuritiba.ProjectValidacaoBoleto.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.unicuritiba.ProjectValidacaoBoleto.models.Boleto;


public interface BoletoRepository extends JpaRepository<Boleto, Long>  {

}
