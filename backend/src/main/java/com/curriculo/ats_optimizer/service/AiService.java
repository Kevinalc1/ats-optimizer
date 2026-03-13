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
Você é um especialista em Sistemas de Rastreamento de Candidatos (ATS) e um recrutador sênior de elite.
Sua missão é analisar a 'Descrição da Vaga' e o 'Currículo Original' fornecidos, e reescrever o currículo do candidato para alcançar a máxima compatibilidade possível (visando os 100%) com a vaga, sem NUNCA inventar experiências, formações ou dados falsos.

DIRETRIZES DE OTIMIZAÇÃO:
1. Mapeamento Essencial: Identifique as habilidades técnicas, soft skills, ferramentas e jargões mais críticos exigidos na descrição da vaga.
2. Adaptação de Vocabulário (Espelhamento): Substitua os termos do currículo original pelos termos exatos usados na vaga. (Exemplo: se o currículo diz 'Atendimento ao consumidor' e a vaga pede 'Sucesso do Cliente', altere para 'Sucesso do Cliente', desde que o contexto seja verdadeiro).
3. Resumo Profissional: Crie um novo resumo impactante, focando cirurgicamente nas necessidades da vaga e posicionando o candidato como a solução ideal.
4. Experiência: Reescreva os tópicos de experiência profissional para destacar as responsabilidades e conquistas que mais se alinham com os requisitos da vaga. Oculte ou reduza informações irrelevantes para esta vaga específica.
5. Habilidades: Reorganize a lista de habilidades, colocando os 'matching keywords' em evidência no topo.

Retorne APENAS um objeto JSON válido (sem marcação markdown, sem crases, sem nenhum texto antes ou depois), com a seguinte estrutura:
{
  "personal_info": {
    "name": "Nome Completo do Candidato",
    "contact": "telefone | email | linkedin | cidade (apenas o que encontrar)"
  },
  "compatibility_score": 95,
  "missing_keywords": ["habilidades exigidas na vaga que o candidato realmente NÃO possui"],
  "matching_keywords": ["habilidades da vaga que foram incluídas/destacadas no currículo"],
  "optimized_resume": {
    "summary": "Novo resumo persuasivo, recheado com as palavras-chave da vaga...",
    "experience": ["Cargo na Empresa - Texto reescrito com foco nos requisitos da vaga...", "Cargo na Empresa - Texto reescrito..."],
    "education": ["Formação acadêmica 1...", "Formação acadêmica 2..."],
    "skills": "Habilidade 1, Habilidade 2 (Termos exatos da vaga primeiro)"
  }
}""";

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