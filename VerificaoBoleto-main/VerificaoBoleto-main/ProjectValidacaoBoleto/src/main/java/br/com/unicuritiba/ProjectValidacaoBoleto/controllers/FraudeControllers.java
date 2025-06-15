package br.com.unicuritiba.ProjectValidacaoBoleto.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.unicuritiba.ProjectValidacaoBoleto.models.Fraude;
import br.com.unicuritiba.ProjectValidacaoBoleto.repositories.FraudeRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/fraude")
public class FraudeControllers {

    @Autowired
    private FraudeRepository fraudeRepository;

    // 🔎 Buscar todas as fraudes
    @GetMapping
    public ResponseEntity<List<Fraude>> getAllFraudes() {
        return ResponseEntity.ok(fraudeRepository.findAll());
    }

    // 🔎 Buscar fraude por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getFraudeById(@PathVariable long id) {
        Optional<Fraude> fraude = fraudeRepository.findById(id);
        return fraude.isPresent() ? ResponseEntity.ok(fraude.get()) : ResponseEntity.notFound().build();
    }

    // 🔎 Buscar fraude por CPF
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<?> getFraudeByCpf(@PathVariable String cpf) {
        Optional<Fraude> fraude = fraudeRepository.findByCpf(cpf);
        return fraude.isPresent() ? ResponseEntity.ok(fraude.get()) : ResponseEntity.notFound().build();
    }

    // 🔎 Buscar fraude por código de barras
    @GetMapping("/codigo/{codigoBarras}")
    public ResponseEntity<?> getFraudeByCodigoBarras(@PathVariable String codigoBarras) {
        Optional<Fraude> fraude = fraudeRepository.findByCodigoBarras(codigoBarras);
        return fraude.isPresent() ? ResponseEntity.ok(fraude.get()) : ResponseEntity.notFound().build();
    }

    // 🔎 Buscar fraude por nome
    @GetMapping("/nome/{nome}")
    public ResponseEntity<?> getFraudeByNome(@PathVariable String nome) {
        Optional<Fraude> fraude = fraudeRepository.findByNome(nome);
        return fraude.isPresent() ? ResponseEntity.ok(fraude.get()) : ResponseEntity.notFound().build();
    }

    // ➕ Cadastrar manualmente uma fraude
    @PostMapping
    public ResponseEntity<Fraude> saveFraude(@RequestBody Fraude fraude) {
        return ResponseEntity.ok(fraudeRepository.save(fraude));
    }

    // 🗑️ Deletar fraude por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFraude(@PathVariable long id) {
        Optional<Fraude> fraude = fraudeRepository.findById(id);
        if (fraude.isPresent()) {
            fraudeRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // 🔄 Atualizar dados da fraude
    @PutMapping("/{id}")
    public ResponseEntity<Fraude> updateFraude(@PathVariable long id, @RequestBody Fraude fraude) {
        Optional<Fraude> existente = fraudeRepository.findById(id);
        if (existente.isPresent()) {
            fraude.setId(id);
            return ResponseEntity.ok(fraudeRepository.save(fraude));
        }
        return ResponseEntity.notFound().build();
    }
}
