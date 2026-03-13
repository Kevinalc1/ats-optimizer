package com.curriculo.ats_optimizer.controller;

import com.curriculo.ats_optimizer.service.AiService;
import com.curriculo.ats_optimizer.service.PdfGeneratorService;
import com.curriculo.ats_optimizer.service.PdfParserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/curriculo")
@CrossOrigin(origins = "*")
@Tag(name = "Otimizador de Currículo ATS", description = "Endpoint para analisar e otimizar currículos em PDF com IA")
public class ResumeController {

    private final PdfParserService pdfParserService;
    private final AiService aiService;
    private final PdfGeneratorService pdfGeneratorService;

    public ResumeController(PdfParserService pdfParserService, AiService aiService, PdfGeneratorService pdfGeneratorService) {
        this.pdfParserService = pdfParserService;
        this.aiService = aiService;
        this.pdfGeneratorService = pdfGeneratorService;
    }

    @PostMapping(value = "/analisar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Otimizar Currículo", description = "Recebe um PDF original e a descrição da vaga, e devolve os dados de compatibilidade e o PDF otimizado em Base64.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Análise concluída e PDF gerado", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Ficheiro inválido ou não enviado"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor ou na IA")
    })
    public ResponseEntity<?> analisarCurriculo(
            @Parameter(description = "O ficheiro PDF do currículo original")
            @RequestParam("arquivo") MultipartFile arquivo,

            @Parameter(description = "O texto com a descrição da vaga alvo")
            @RequestParam("vaga") String vaga) {

        if (arquivo.isEmpty() || !"application/pdf".equals(arquivo.getContentType())) {
            return ResponseEntity.badRequest().body("Erro: Por favor, envie um ficheiro PDF válido.");
        }

        try {
            System.out.println("1. A ler o PDF original...");
            String textoExtraido = pdfParserService.extrairTextoPdf(arquivo);

            System.out.println("2. A enviar para a IA do Groq...");
            String jsonDaIA = aiService.otimizarCurriculo(vaga, textoExtraido);

            System.out.println("3. A gerar o novo PDF otimizado...");
            byte[] novoPdf = pdfGeneratorService.gerarPdfOtimizado(jsonDaIA);

            System.out.println("4. A empacotar dados para o Frontend...");
            String base64Pdf = Base64.getEncoder().encodeToString(novoPdf);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode aiResult = mapper.readTree(jsonDaIA);

            Map<String, Object> responseData = new HashMap<>();

            int score = aiResult.has("compatibility_score") ? aiResult.get("compatibility_score").asInt() : 0;

            responseData.put("score", score);
            responseData.put("matching_keywords", aiResult.get("matching_keywords"));
            responseData.put("missing_keywords", aiResult.get("missing_keywords"));
            responseData.put("pdfBase64", base64Pdf);

            return ResponseEntity.ok(responseData);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", "Ocorreu um erro: " + e.getMessage()));
        }
    }
}