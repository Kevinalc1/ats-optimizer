package com.curriculo.ats_optimizer.controller;

import com.curriculo.ats_optimizer.service.AiService;
import com.curriculo.ats_optimizer.service.PdfGeneratorService;
import com.curriculo.ats_optimizer.service.PdfParserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/curriculo")
@CrossOrigin(origins = "*")
@Tag(name = "Otimizador de Currículo ATS", description = "Endpoint para analisar e otimizar currículos em PDF com IA")
public class ResumeController{

    private final PdfParserService pdfParserService;
    private final AiService aiService;
    private final PdfGeneratorService pdfGeneratorService;

    public ResumeController(PdfParserService pdfParserService, AiService aiService, PdfGeneratorService pdfGeneratorService) {
        this.pdfParserService = pdfParserService;
        this.aiService = aiService;
        this.pdfGeneratorService = pdfGeneratorService;
    }

    @PostMapping(value = "/analisar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Otimizar Currículo", description = "Recebe um PDF original e a descrição da vaga, e devolve um novo PDF otimizado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PDF otimizado gerado com sucesso",
                    content = @Content(mediaType = "application/pdf")),
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

            System.out.println("4. Sucesso! A enviar ficheiro para o utilizador.");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "Curriculo_Otimizado.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(novoPdf);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Ocorreu um erro: " + e.getMessage());
        }
    }
}