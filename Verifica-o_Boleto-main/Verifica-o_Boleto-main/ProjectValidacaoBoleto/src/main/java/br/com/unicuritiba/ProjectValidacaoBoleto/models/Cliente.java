package br.com.unicuritiba.ProjectValidacaoBoleto.models;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Cliente {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String nome;
	private String email;
	private int age;
	private String password;
	private int CPF;
	
	@OneToMany

	
	private List<Empresa> empresa;
	private List<Boleto> boleto;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getCPF() {
		return CPF;
	}
	public void setCPF(int cPF) {
		CPF = cPF;
	}
	public List<Empresa> getBanco() {
		return banco;
	}
	public void setBanco(List<Empresa> banco) {
		this.banco = banco;
	}
	public List<Boleto> getBoleto() {
		return boleto;
	}
	public void setBoleto(List<Boleto> boleto) {
		this.boleto = boleto;
	}
	

}
