package br.com.carloshenreis.controller;

import java.io.IOException;


import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.sourceforge.tess4j.Tesseract;

@RestController
@RequestMapping("/ocr")
public class OcrController {
	
	@Value("${tesseract.path}") private String path;
	
	@PostMapping()
	@CrossOrigin(origins = "*")
	public ResponseEntity<String> traduzir(@RequestParam(name="file") MultipartFile file) throws Exception{
		String ext = FilenameUtils.getExtension(file.getOriginalFilename()); 	
		if (!"png".equals(ext.toLowerCase()) && !"jpg".equals(ext.toLowerCase()) && !"gif".equals(ext.toLowerCase())) {
			return ResponseEntity.badRequest().build();
		}
		String resultado = "";

		Tesseract tesseract = new Tesseract();
		resultado = "";
		tesseract.setLanguage("eng");
		return ResponseEntity.ok(resultado);
	}
}