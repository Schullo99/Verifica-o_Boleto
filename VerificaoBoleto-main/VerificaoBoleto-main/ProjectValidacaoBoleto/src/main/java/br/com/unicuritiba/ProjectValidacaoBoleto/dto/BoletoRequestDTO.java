package br.com.unicuritiba.ProjectValidacaoBoleto.dto;

public class BoletoRequestDTO {
    private String linhaDigitavel;
    private int codigoBanco;
    private String cpfCliente;
    private String valor;
    private String vencimento;
    
    
	public String getLinhaDigitavel() {
		return linhaDigitavel;
	}
	public void setLinhaDigitavel(String linhaDigitavel) {
		this.linhaDigitavel = linhaDigitavel;
	}
	public int getCodigoBanco() {
		return codigoBanco;
	}
	public void setCodigoBanco(int codigoBanco) {
		this.codigoBanco = codigoBanco;
	}
	public String getCpfCliente() {
		return cpfCliente;
	}
	public void setCpfCliente(String cpfCliente) {
		this.cpfCliente = cpfCliente;
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

    // Getters e Setters
    
}
