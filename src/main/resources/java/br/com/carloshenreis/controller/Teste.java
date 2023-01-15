package br.com.carloshenreis.controller;

import br.com.carloshenreis.config.fonts.AutoFont;
import com.openhtmltopdf.objects.zxing.ZXingObjectDrawer;
import com.openhtmltopdf.render.DefaultObjectDrawerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

@RestController
@RequestMapping("/pdf")
public class Teste {

    @GetMapping
    public ResponseEntity<byte[]> teste() {

        try {
            // HTML file - Input
            File inputHTML = new File(getClass().getClassLoader().getResource("templates/index.html").getFile());


            org.w3c.dom.Document doc = this.createWellFormedHtml(inputHTML);
            System.out.println("Starting conversion to PDF...");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            inputHTML.
            return new ResponseEntity<>(this.xhtmlToPdf(doc), headers, HttpStatus.OK);
        } catch (IOException e) {
            System.out.println("Error while converting HTML to PDF " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Creating well formed document
    private org.w3c.dom.Document createWellFormedHtml(File inputHTML) throws IOException {
        Document document = Jsoup.parse(inputHTML, "UTF-8");
        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        System.out.println("HTML parsing done...");
        return new W3CDom().fromJsoup(document);
    }

    private byte[] xhtmlToPdf(org.w3c.dom.Document doc) throws IOException {
        // base URI to resolve future resources
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.toStream(os);
        // add external font
        this.getAllFonts(builder);
        this.generateBarcode(builder);
        builder.withW3cDocument(doc, "/");
        builder.run();
        System.out.println("PDF creation completed");
        return os.toByteArray();
    }

    public void getAllFonts(PdfRendererBuilder builder) throws IOException {
        Path fontDirectory = new File(getClass().getClassLoader().getResource("fonts").getFile()).toPath();

        // PERF: Should only be called once, as each font must be parsed for font family name.
        List<AutoFont.CSSFont> fonts = AutoFont.findFontsInDirectory(fontDirectory);

        // Use this in your template for the font-family property.
        String fontFamily = AutoFont.toCSSEscapedFontFamily(fonts);

        // Add fonts to builder.
        AutoFont.toBuilder(builder, fonts);
    }

    private void generateBarcode(PdfRendererBuilder builder) {
        DefaultObjectDrawerFactory factory = new DefaultObjectDrawerFactory();
        factory.registerDrawer("image/barcode", new ZXingObjectDrawer());
        builder.useObjectDrawerFactory(factory);
    }
}
