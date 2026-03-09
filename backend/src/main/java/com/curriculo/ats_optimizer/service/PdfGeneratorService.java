package com.curriculo.ats_optimizer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;

@Service
public class PdfGeneratorService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public byte[] gerarPdfOtimizado(String jsonDaIA) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             Document document = new Document(PageSize.A4, 50, 50, 50, 50)) { // Margens mais elegantes

            PdfWriter.getInstance(document, outputStream);
            document.open();

            JsonNode rootNode = objectMapper.readTree(jsonDaIA);
            JsonNode infoPessoal = rootNode.path("personal_info");
            JsonNode curriculoNode = rootNode.path("optimized_resume");

            Font nomeFont = new Font(Font.HELVETICA, 22, Font.BOLD, Color.BLACK);
            Font contatoFont = new Font(Font.HELVETICA, 10, Font.NORMAL, Color.DARK_GRAY);
            Font tituloSecaoFont = new Font(Font.HELVETICA, 14, Font.BOLD, new Color(0, 51, 102)); // Azul escuro
            Font textoFont = new Font(Font.HELVETICA, 11, Font.NORMAL, Color.BLACK);
            Font bulletFont = new Font(Font.HELVETICA, 11, Font.BOLD, Color.BLACK);

            LineSeparator linha = new LineSeparator(1f, 100f, Color.LIGHT_GRAY, Element.ALIGN_CENTER, -2f);

            Paragraph nome = new Paragraph(infoPessoal.path("name").asText("Nome do Candidato").toUpperCase(), nomeFont);
            nome.setAlignment(Element.ALIGN_CENTER);
            document.add(nome);

            Paragraph contato = new Paragraph(infoPessoal.path("contact").asText(""), contatoFont);
            contato.setAlignment(Element.ALIGN_CENTER);
            document.add(contato);

            document.add(new Chunk(linha));
            document.add(new Paragraph("\n"));

            document.add(new Paragraph("RESUMO PROFISSIONAL", tituloSecaoFont));
            document.add(new Paragraph(curriculoNode.path("summary").asText(), textoFont));
            document.add(new Paragraph("\n"));

            document.add(new Paragraph("EXPERIÊNCIA PROFISSIONAL", tituloSecaoFont));
            document.add(new Chunk(linha));
            for (JsonNode exp : curriculoNode.path("experience")) {
                Paragraph expParagrafo = new Paragraph();
                expParagrafo.add(new Chunk("• ", bulletFont));
                expParagrafo.add(new Chunk(exp.asText(), textoFont));
                expParagrafo.setSpacingBefore(5f);
                document.add(expParagrafo);
            }
            document.add(new Paragraph("\n"));

            if (curriculoNode.has("education")) {
                document.add(new Paragraph("FORMAÇÃO ACADÊMICA", tituloSecaoFont));
                document.add(new Chunk(linha));
                for (JsonNode edu : curriculoNode.path("education")) {
                    Paragraph eduParagrafo = new Paragraph();
                    eduParagrafo.add(new Chunk("• ", bulletFont));
                    eduParagrafo.add(new Chunk(edu.asText(), textoFont));
                    eduParagrafo.setSpacingBefore(5f);
                    document.add(eduParagrafo);
                }
                document.add(new Paragraph("\n"));
            }

            document.add(new Paragraph("HABILIDADES TÉCNICAS E FERRAMENTAS", tituloSecaoFont));
            document.add(new Chunk(linha));
            document.add(new Paragraph(curriculoNode.path("skills").asText(), textoFont));

            document.close();
            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar o PDF estilizado: " + e.getMessage());
        }
    }
}