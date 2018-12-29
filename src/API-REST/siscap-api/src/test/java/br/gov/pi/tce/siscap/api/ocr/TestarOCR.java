package br.gov.pi.tce.siscap.api.ocr;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.pdfbox.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import br.gov.pi.tce.siscap.api.service.ocr.GoogleVisionImage2Text;
import br.gov.pi.tce.siscap.api.service.ocr.Image2Text;

public class TestarOCR {

	public static void main(String[] args) throws Exception {
		File diario = new ClassPathResource("printMunicipios.png").getFile();
		InputStream in = new FileInputStream(diario);
		
		byte[] conteudo = IOUtils.toByteArray(in);

		Image2Text image2Text = new GoogleVisionImage2Text();
		String text = image2Text.convertToText(conteudo);
		System.out.println(text);
	}

}
