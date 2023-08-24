package com.example.demo.util;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.*;
import java.util.List;

public class PDFGenerator {

    public static void generatePDF(String htmlContent, OutputStream outputStream) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);

        document.open();
        document.addAuthor("Your Author Name");
        document.addTitle("Employee Details");

        // Convert HTML content to a Reader
        StringReader reader = new StringReader(htmlContent);

        List<?> parsedHtmlElements = HTMLWorker.parseToList(reader, null);

        for (Object element : parsedHtmlElements) {
            document.add((com.itextpdf.text.Element) element);
        }

        document.close();
    }
}