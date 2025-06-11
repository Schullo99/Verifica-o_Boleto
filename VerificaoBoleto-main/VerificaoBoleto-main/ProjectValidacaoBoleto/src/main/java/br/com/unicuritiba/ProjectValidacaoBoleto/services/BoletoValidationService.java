package br.com.unicuritiba.ProjectValidacaoBoleto.services;

import br.com.unicuritiba.ProjectValidacaoBoleto.models.Boleto;
import br.com.unicuritiba.ProjectValidacaoBoleto.models.Empresa;
import br.com.unicuritiba.ProjectValidacaoBoleto.models.Fraude;
import br.com.unicuritiba.ProjectValidacaoBoleto.dto.ValidationResponse; // Importar o DTO
import br.com.unicuritiba.ProjectValidacaoBoleto.repositories.BoletoRepository;
import br.com.unicuritiba.ProjectValidacaoBoleto.repositories.EmpresaRepository;
import br.com.unicuritiba.ProjectValidacaoBoleto.repositories.FraudeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class BoletoValidationService {

    @Autowired
    private GoogleVisionService googleVisionService;

    @Autowired
    private BoletoRepository boletoRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private FraudeRepository fraudeRepository;

    public ValidationResponse validateBoleto(String imagemBase64) { // Remover boletoId
        // 1. Processar a imagem e extrair informações
        Boleto extractedBoleto = googleVisionService.processarImagem(imagemBase64);

        if (extractedBoleto == null) {
            return new ValidationResponse(false, "Erro ao processar a imagem do boleto.", null);
        }

        boolean isFraud = false;
        StringBuilder fraudReason = new StringBuilder("Inconsistências/Padrões de Fraude: ");

        // --- VALIDAÇÕES DE FORMATO E CONSISTÊNCIA BÁSICA ---

        // 1. Linha Digitável
        if (extractedBoleto.getlinhaDigitavel() == null || !extractedBoleto.getlinhaDigitavel().matches("\\d{5}\\.\\d{5} \\d{5}\\.\\d{6} \\d{5}\\.\\d{6} \\d \\d{14}")) {
            isFraud = true;
            fraudReason.append("Formato da Linha Digitável inválido. ");
        } else {
            // Opcional: Implementar a validação do dígito verificador da linha digitável aqui
            // É uma validação complexa, mas muito importante para a segurança.
            // Ex: isLinhaDigitavelValida(extractedBoleto.getlinhaDigitavel())
            // Se falhar, setar isFraud = true; fraudReason.append("Dígito verificador da linha digitável inválido. ");
        }

        // 2. CPF (simples validação de formato)
        if (extractedBoleto.getcpf() == null || !extractedBoleto.getcpf().matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")) {
             // Nota: Para uma validação de CPF robusta, você precisaria de um algoritmo de validação de CPF.
            isFraud = true;
            fraudReason.append("Formato do CPF inválido. ");
        }

        // 3. Vencimento
        LocalDate vencimentoDate = null;
        if (extractedBoleto.getVencimento() == null || !extractedBoleto.getVencimento().matches("\\d{2}/\\d{2}/\\d{4}")) {
            isFraud = true;
            fraudReason.append("Formato da Data de Vencimento inválido. ");
        } else {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                vencimentoDate = LocalDate.parse(extractedBoleto.getVencimento(), formatter);
                if (vencimentoDate.isBefore(LocalDate.now().minusYears(1))) { // Ex: boleto muito antigo
                    isFraud = true;
                    fraudReason.append("Data de vencimento muito antiga. ");
                }
            } catch (DateTimeParseException e) {
                isFraud = true;
                fraudReason.append("Erro ao analisar a Data de Vencimento. ");
            }
        }

        // 4. Valor (apenas checar se foi extraído)
        if (extractedBoleto.getValor() == null || extractedBoleto.getValor().isEmpty()) {
            isFraud = true;
            fraudReason.append("Valor do boleto não encontrado. ");
        }

        // --- VALIDAÇÕES CONTRA DADOS EM BLACKLIST/FRAUDE ---

        // 5. Verificar CPF em Fraudes
        if (extractedBoleto.getcpf() != null) {
            Optional<Fraude> fraudePorCpf = fraudeRepository.findByCPF(extractedBoleto.getcpf());
            if (fraudePorCpf.isPresent()) {
                isFraud = true;
                fraudReason.append("CPF do recebedor encontrado na lista de fraudes. ");
            }
        }

        // 6. Verificar Linha Digitável em Fraudes
        if (extractedBoleto.getlinhaDigitavel() != null) {
            Optional<Fraude> fraudePorLinhaDigitavel = fraudeRepository.findByCodigoBarras(extractedBoleto.getlinhaDigitavel());
            if (fraudePorLinhaDigitavel.isPresent()) {
                isFraud = true;
                fraudReason.append("Linha Digitável encontrada na lista de fraudes. ");
            }
        }

        // 7. Verificar Nome do Recebedor em Fraudes (cuidado com falsos positivos)
        if (extractedBoleto.getNome() != null && !extractedBoleto.getNome().isEmpty()) {
            Optional<Fraude> fraudePorNome = fraudeRepository.findByNome(extractedBoleto.getNome());
            if (fraudePorNome.isPresent()) {
                isFraud = true;
                fraudReason.append("Nome do recebedor encontrado na lista de fraudes. ");
            }
        }

        // 8. Verificar se o Código do Banco é válido (ex: lista de bancos conhecidos)
        // Para uma validação mais robusta, você precisaria de uma lista ou API de bancos brasileiros.
        // Por enquanto, apenas um exemplo básico:
        if (extractedBoleto.getCodigo_banco() == 0 || String.valueOf(extractedBoleto.getCodigo_banco()).length() != 3) {
             isFraud = true;
             fraudReason.append("Código do banco inválido ou não detectado. ");
        }
        // Opcional: Consultar empresa por CNPJ se extraído ou por nome do boleto, para ver se está na blacklist
        // Se você tiver uma maneira de extrair o CNPJ da imagem (regex mais complexo)
        // ou quiser associar o nome extraído a uma empresa existente:
        if (extractedBoleto.getNome() != null && !extractedBoleto.getNome().isEmpty()) {
             Optional<Empresa> empresaAssociada = empresaRepository.findByNome(extractedBoleto.getNome());
             if (empresaAssociada.isPresent() && empresaAssociada.get().isInBlacklist()) {
                 isFraud = true;
                 fraudReason.append("Empresa associada ao nome do recebedor está na blacklist. ");
             }
        }


        // --- FINALIZAÇÃO E PERSISTÊNCIA ---

        // Marcar e salvar o boleto como fraude, se aplicável
        extractedBoleto.setFraudulent(isFraud);
        boletoRepository.save(extractedBoleto); // Sempre salva o boleto analisado, com status de fraude ou não

        String finalMessage;
        if (isFraud) {
            // Se for fraude, registrar na tabela de fraudes
            Fraude fraude = new Fraude();
            fraude.setCodigoBarras(extractedBoleto.getlinhaDigitavel());
            fraude.setCPF(extractedBoleto.getcpf());
            fraude.setNome(extractedBoleto.getNome());
            fraude.setDataValidade(extractedBoleto.getVencimento());
            // Você pode adicionar mais detalhes da fraude aqui, como os 'fraudReason.toString()'
            // fraude.setDescricaoDetalhada(fraudReason.toString()); // Se adicionar um campo de descrição em Fraude
            fraude.setCodigoBanco(String.valueOf(extractedBoleto.getCodigo_banco()));
            fraudeRepository.save(fraude);

            // Opcional: Se houver uma forma de associar o boleto a uma empresa, marque a empresa
            // extraída da imagem (ou a que você inferiu ser a pagadora) como blacklist.
            // Isso exigiria uma lógica mais avançada de identificação da empresa pelo boleto.
            // Por agora, vamos manter a lógica de marcar a empresa associada ao boleto se houver um relacionamento.
            Empresa empresaAssociadaAoBoleto = extractedBoleto.getEmpresa(); // Se houver um ManyToOne setado em extractedBoleto
            if (empresaAssociadaAoBoleto != null) {
                empresaAssociadaAoBoleto.setInBlacklist(true);
                empresaAssociadaAoBoleto.setCont_fraudes(empresaAssociadaAoBoleto.getCont_fraudes() + 1);
                empresaRepository.save(empresaAssociadaAoBoleto);
            }

            finalMessage = "Boleto suspeito de fraude! Detalhes: " + fraudReason.toString().trim();
        } else {
            finalMessage = "Boleto parece ser seguro para transferência.";
        }

        return new ValidationResponse(!isFraud, finalMessage, extractedBoleto);
    }

    // Você pode adicionar métodos auxiliares para validações mais complexas, como:
    // private boolean isValidCpf(String cpf) { /* lógica de validação de CPF */ return true; }
    // private boolean isValidLinhaDigitavelChecksum(String linhaDigitavel) { /* lógica de dígito verificador */ return true; }
}