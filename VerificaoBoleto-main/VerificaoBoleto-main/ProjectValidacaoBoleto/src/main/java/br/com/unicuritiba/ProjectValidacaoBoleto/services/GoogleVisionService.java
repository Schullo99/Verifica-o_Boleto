package br.com.unicuritiba.ProjectValidacaoBoleto.services;

import br.com.unicuritiba.ProjectValidacaoBoleto.models.Boleto;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File; // Para trabalhar com arquivos temporários
import java.io.IOException;
import java.util.Base64;
import java.util.regex.*;
import javax.imageio.ImageIO; // Para converter byte[] em BufferedImage

@Service
public class GoogleVisionService { // O nome da classe pode ser alterado para "TesseractService"

    // Não precisamos mais da API_KEY ou URL do Google Vision
    // private final String API_KEY = "AIzaSyAci3p3O3XHCc4eVPxLjfnMyFQaUJzmWBc";
    // private final String URL = "https://vision.googleapis.com/v1/images:annotate?key=";

    public Boleto processarImagem(String imagemBase64) {
        String textoExtraido = "";
        try {
            // 1. Converter a imagem Base64 para BufferedImage
            byte[] decodedBytes = Base64.getDecoder().decode(imagemBase64);
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(decodedBytes));

            // 2. Criar uma instância do Tesseract
            ITesseract instance = new Tesseract();

            // 3. Configurar o caminho para os arquivos de dados do Tesseract (tessdata)
            // VOCÊ PRECISA MUDAR ESTE CAMINHO PARA ONDE VOCÊ INSTALOU O TESSERACT
            // E ONDE ESTÃO SEUS ARQUIVOS DE IDIOMA (por.traineddata, eng.traineddata, etc.)
            // Exemplo: se você instalou o Tesseract em C:\Program Files\Tesseract-OCR\tessdata
            // instance.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");
            // Ou se você colocar os arquivos tessdata em resources:
            // instance.setDatapath("src/main/resources/tessdata");
            // É recomendado usar um caminho absoluto ou configurável via environment variable.
            instance.setDatapath("C:\\Users\\<SeuUsuario>\\tesseract\\tessdata"); // Exemplo para Windows, altere conforme sua instalação!

            // 4. Configurar o idioma (certifique-se de ter o arquivo 'por.traineddata' na pasta tessdata)
            instance.setLanguage("por"); // 'por' para Português

            // 5. Executar o OCR
            textoExtraido = instance.doOCR(bufferedImage);

        } catch (TesseractException e) {
            e.printStackTrace();
            System.err.println("Erro no OCR com Tesseract: " + e.getMessage());
            throw new RuntimeException("Falha no OCR com Tesseract: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erro ao ler a imagem: " + e.getMessage());
            throw new RuntimeException("Falha ao ler a imagem para OCR: " + e.getMessage());
        }

        Boleto boleto = extrairInformacoes(textoExtraido);
        return boleto;
    }

    private Boleto extrairInformacoes(String texto) {
        // Seus regexes existentes, que operam no texto extraído, permanecem os mesmos
        String linhaDigitavel = encontrarRegex(texto, "\\d{5}\\.\\d{5} \\d{5}\\.\\d{6} \\d{5}\\.\\d{6} \\d \\d{14}");
        String cpf = encontrarRegex(texto, "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}");
        String nomeBeneficiario = encontrarRegex(texto, "(?i)(?:Benefici[aá]rio|Cedente|Favorecido|Nome):\\s*([\\p{L}\\s.,]+)");
        String valor = encontrarRegex(texto, "R\\$ ?(\\d{1,3}(?:[.,]\\d{3})*(?:[.,]\\d{2})?)");
        String vencimento = encontrarRegex(texto, "(?:Vencimento:|Data de Vencimento)[:\\s]*(\\d{2}/\\d{2}/\\d{4})");

        if (nomeBeneficiario != null && nomeBeneficiario.contains(":")) {
            nomeBeneficiario = nomeBeneficiario.split(":", 2)[1].trim();
        }

        Boleto boleto = new Boleto();
        boleto.setLinhaDigitavel(linhaDigitavel);
        boleto.setCpfRecebedor(cpf);
        boleto.setNomeRecebedor(nomeBeneficiario);
        boleto.setValor(valor);
        boleto.setVencimento(vencimento);

        if (linhaDigitavel != null && linhaDigitavel.length() >= 3) {
            try {
                boleto.setCodigo_banco(Integer.parseInt(linhaDigitavel.substring(0, 3)));
            } catch (NumberFormatException e) {
                boleto.setCodigo_banco(0);
            }
        } else {
            boleto.setCodigo_banco(0);
        }

        return boleto;
    }

    private String encontrarRegex(String texto, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(texto);
        if (matcher.find()) {
            if (matcher.groupCount() >= 1) {
                return matcher.group(1);
            }
            return matcher.group(0);
        }
        return null;
    }
}