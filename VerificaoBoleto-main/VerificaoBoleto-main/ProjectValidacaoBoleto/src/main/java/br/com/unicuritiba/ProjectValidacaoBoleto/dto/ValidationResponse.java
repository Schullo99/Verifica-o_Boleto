package br.com.unicuritiba.ProjectValidacaoBoleto.dto;

import br.com.unicuritiba.ProjectValidacaoBoleto.models.Boleto;

public class ValidationResponse {
    private boolean isSafe;
    private String message;
    private Boleto validatedBoleto; // Opcional: para retornar o boleto processado com detalhes

    public ValidationResponse(boolean isSafe, String message, Boleto validatedBoleto) {
        this.isSafe = isSafe;
        this.message = message;
        this.validatedBoleto = validatedBoleto;
    }

    // Getters e Setters
    public boolean isSafe() {
        return isSafe;
    }

    public void setSafe(boolean safe) {
        isSafe = safe;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boleto getValidatedBoleto() {
        return validatedBoleto;
    }

    public void setValidatedBoleto(Boleto validatedBoleto) {
        this.validatedBoleto = validatedBoleto;
    }
}