package br.com.unicuritiba.ProjectValidacaoBoleto.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Boleto {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	
	private long id;
	private String linhaDigitavel;
	private int codigo_banco;
	private String nome;
	private String cpf;
	private String vencimento;
	private String Rua;
	private String Bairro;
	private String Cidade;
	private String CEP;
	private String valor;
	private boolean isFraudulent; // Novo campo para indicar fraude
	private String extractedLinhaDigitavel; // Novo campo para linha digitável extraída pela API
	private String extractedCpf; // Novo campo para CPF extraído pela API
	private String extractedName; // Novo campo para nome extraído pela API
	private String extractedVencimento; // Novo campo para vencimento extraído pela API


	@ManyToOne
	private Empresa empresa;
	
	@ManyToOne
	private Cliente cliente;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getlinhaDigitavel() {
		return linhaDigitavel;
	}
	public void setLinhaDigitavel(String linhaDigitavel) {
		this.linhaDigitavel = linhaDigitavel;
	}
	public int getCodigo_banco() {
		return codigo_banco;
	}
	public void setCodigo_banco(int codigo_banco) {
		this.codigo_banco = codigo_banco;
	}
	public String getNome() {
		return nome;
	}
	public void setNomeRecebedor(String nome) { // Renomeado para setNomeRecebedor para maior clareza
		this.nome = nome;
	}
	public String getcpf() {
		return cpf;
	}
	public void setCpfRecebedor(String cpf) { // Renomeado para setCpfRecebedor para maior clareza
		this.cpf = cpf;
	}

	public String getRua() {
		return Rua;
	}
	public void setRua(String rua) {
		Rua = rua;
	}
	public String getBairro() {
		return Bairro;
	}
	public void setBairro(String bairro) {
		Bairro = bairro;
	}
	public String getCidade() {
		return Cidade;
	}
	public void setCidade(String cidade) {
		Cidade = cidade;
	}
	public String getCEP() {
		return CEP;
	}
	public void setCEP(String cEP) {
		CEP = cEP;
	
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getVencimento() {
		return vencimento;
	}
	public void setVencimento(String vencimento) {
		this.vencimento = vencimento;
	}

	public boolean isFraudulent() {
		return isFraudulent;
	}

	public void setFraudulent(boolean fraudulent) {
		isFraudulent = fraudulent;
	}

	public String getExtractedLinhaDigitavel() {
		return extractedLinhaDigitavel;
	}

	public void setExtractedLinhaDigitavel(String extractedLinhaDigitavel) {
		this.extractedLinhaDigitavel = extractedLinhaDigitavel;
	}

	public String getExtractedCpf() {
		return extractedCpf;
	}

	public void setExtractedCpf(String extractedCpf) {
		this.extractedCpf = extractedCpf;
	}

	public String getExtractedName() {
		return extractedName;
	}

	public void setExtractedName(String extractedName) {
		this.extractedName = extractedName;
	}

	public String getExtractedVencimento() {
		return extractedVencimento;
	}

	public void setExtractedVencimento(String extractedVencimento) {
		this.extractedVencimento = extractedVencimento;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
}