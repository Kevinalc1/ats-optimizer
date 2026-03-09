package com.curriculo.ats_optimizer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

@Service
public class AiService {

    @Value("${groq.api.key}")
    private String apiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String otimizarCurriculo(String vaga, String curriculo) {
        String url = "https://api.groq.com/openai/v1/chat/completions";

        String promptSistema = """
            Você é um recrutador sênior especialista em sistemas ATS.
            Analise a vaga e o currículo fornecidos e reescreva o currículo para otimizar as palavras-chave.
            Extraia também os dados pessoais do candidato encontrados no texto.
            NÃO invente experiências falsas.
            Retorne APENAS um objeto JSON válido (sem markdown), com a seguinte estrutura:
            {
              "personal_info": {
                "name": "Nome Completo do Candidato",
                "contact": "telefone | email | linkedin | cidade"
              },
              "compatibility_score": 85,
              "missing_keywords": ["skill1", "skill2"],
              "matching_keywords": ["skill3", "skill4"],
              "optimized_resume": {
                "summary": "Novo resumo otimizado...",
                "experience": ["Experiência 1 focada na vaga...", "Experiência 2..."],
                "education": ["Formação acadêmica 1...", "Formação acadêmica 2..."],
                "skills": "Nova lista de skills"
              }
            }
            """;

        String promptUsuario = "VAGA:\n" + vaga + "\n\nCURRÍCULO ORIGINAL:\n" + curriculo;

        try {
            Map<String, Object> requestBody = Map.of(
                    "model", "llama-3.3-70b-versatile",
                    "messages", List.of(
                            Map.of("role", "system", "content", promptSistema),
                            Map.of("role", "user", "content", promptUsuario)
                    ),
                    "response_format", Map.of("type", "json_object"), // Força a saída em JSON
                    "temperature", 0.7
            );

            String jsonBody = objectMapper.writeValueAsString(requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey.trim()) // O .trim() salva vidas!
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonNode rootNode = objectMapper.readTree(response.body());

            if (rootNode.has("error")) {
                return "Erro do Groq: " + rootNode.path("error").path("message").asText();
            }

            return rootNode.path("choices").get(0).path("message").path("content").asText();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao comunicar com o Groq: " + e.getMessage());
        }
    }
}