package com.curriculo.ats_optimizer.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class PdfParserService {

    public String extrairTextoPdf(MultipartFile arquivo) {
        try (InputStream inputStream = arquivo.getInputStream();
             PDDocument documento = Loader.loadPDF(inputStream.readAllBytes())) {

            PDFTextStripper extrator = new PDFTextStripper();
            return extrator.getText(documento);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao extrair texto do PDF: " + e.getMessage());
        }
    }
}
