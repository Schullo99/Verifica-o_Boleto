package br.com.unicuritiba.ProjectValidacaoBoleto.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

@RestController
public class ValidadorController {

    @PostMapping("/verificarBoleto")
    public Map<String, String> verificarBoleto(@RequestBody Map<String, String> dados) {

        String codigoBarras = dados.get("codigoBarras");
        String bancoInformado = dados.get("banco");
        String cpf = dados.get("cpf");
        String nome = dados.get("nome");
        String vencimento = dados.get("vencimento");
        String valor = dados.get("valor");

        Map<String, String> resposta = new HashMap<>();

        // 🔍 Verificações básicas:
        boolean valido = true;
        String mensagem = "Boleto válido";

        // ✔️ Verifica se o código de barras tem 47 ou 48 caracteres:
        if (codigoBarras == null || (codigoBarras.length() != 47 && codigoBarras.length() != 48)) {
            mensagem = "Código de barras inválido.";
            valido = false;
        }

        // ✔️ Verifica se o banco corresponde (os 3 primeiros dígitos do código de barras):
        String bancoCodigoBarras = codigoBarras.substring(0, 3);
        if (!bancoCodigoBarras.equals(bancoInformado)) {
            mensagem = "Código do banco não corresponde.";
            valido = false;
        }

        // ✔️ Verifica se o CPF tem 11 dígitos:
        if (cpf == null || cpf.length() != 11) {
            mensagem = "CPF inválido.";
            valido = false;
        }

        // ✔️ Verifica se o vencimento não está vencido:
        try {
            LocalDate dataVencimento = LocalDate.parse(vencimento, DateTimeFormatter.ISO_LOCAL_DATE);
            if (dataVencimento.isBefore(LocalDate.now())) {
                mensagem = "Boleto vencido.";
                valido = false;
            }
        } catch (Exception e) {
            mensagem = "Data de vencimento inválida.";
            valido = false;
        }

        // ✔️ Verifica se o valor é válido (número):
        try {
            Double.parseDouble(valor.replace(",", "."));
        } catch (Exception e) {
            mensagem = "Valor inválido.";
            valido = false;
        }

        // ✅ Resposta:
        if (valido) {
            resposta.put("status", "Válido");
        } else {
            resposta.put("status", "Fraude");
        }
        resposta.put("mensagem", mensagem);

        return resposta;
    }
}
