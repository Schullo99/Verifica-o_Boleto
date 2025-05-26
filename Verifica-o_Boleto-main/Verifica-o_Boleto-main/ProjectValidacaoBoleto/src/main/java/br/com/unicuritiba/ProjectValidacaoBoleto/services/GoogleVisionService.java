package br.com.unicuritiba.ProjectValidacaoBoleto.services;

import br.com.unicuritiba.ProjectValidacaoBoleto.models.Boleto;
import br.com.unicuritiba.ProjectValidacaoBoleto.repositories.BoletoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.regex.*;

@Service
public class GoogleVisionService {

    private final String API_KEY = "AIzaSyAci3p3O3XHCc4eVPxLjfnMyFQaUJzmWBc"; 
    private final String URL = "https://vision.googleapis.com/v1/images:annotate?key=" + API_KEY;

    @Autowired
    private BoletoRepository boletoRepository;

    public Boleto processarImagem(String imagemBase64) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> image = Map.of("content", imagemBase64);
        Map<String, Object> feature = Map.of("type", "DOCUMENT_TEXT_DETECTION");
        Map<String, Object> request = Map.of("image", image, "features", List.of(feature));
        Map<String, Object> body = Map.of("requests", List.of(request));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                URL, HttpMethod.POST, entity, String.class);

        String textoExtraido = "";
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            textoExtraido = root.at("/responses/0/fullTextAnnotation/text").asText();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Boleto boleto = extrairInformacoes(textoExtraido);
        return boletoRepository.save(boleto);
    }

    private Boleto extrairInformacoes(String texto) {
        String linhaDigitavel = encontrarRegex(texto, "\\d{5}\\.\\d{5} \\d{5}\\.\\d{6} \\d{5}\\.\\d{6} \\d \\d{14}");
        String cpf = encontrarRegex(texto, "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}");
        String nome = encontrarRegex(texto, "(?i)(Benefici[áa]rio|Cedente|Favorecido):?\\s*(.+)");
        String valor = encontrarRegex(texto, "R\\$ ?(\\d+[.,]?\\d*)");
        String vencimento = encontrarRegex(texto, "Vencimento:? ?(\\d{2}/\\d{2}/\\d{4})");

        if (nome != null && nome.contains(":")) nome = nome.split(":", 2)[1].trim();

        Boleto boleto = new Boleto();
        boleto.setLinhaDigitavel(linhaDigitavel);
        boleto.setCpfRecebedor(cpf);
        boleto.setNomeRecebedor(nome);
        boleto.setValor(valor);
        boleto.setVencimento(vencimento);
        return boleto;
    }

    private String encontrarRegex(String texto, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(texto);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }
}
