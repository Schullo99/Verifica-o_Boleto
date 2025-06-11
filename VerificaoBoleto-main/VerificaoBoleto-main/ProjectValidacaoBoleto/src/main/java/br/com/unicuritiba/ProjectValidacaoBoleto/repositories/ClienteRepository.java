package br.com.unicuritiba.ProjectValidacaoBoleto.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.unicuritiba.ProjectValidacaoBoleto.models.Cliente;


public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}