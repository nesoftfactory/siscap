package br.gov.pi.tce.siscap.api.ocr;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.pdfbox.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.gov.pi.tce.siscap.api.service.ocr.GoogleVisionImage2Text;
import br.gov.pi.tce.siscap.api.service.ocr.Image2Text;
import br.gov.pi.tce.siscap.api.service.ocr.PDF2Images;
import br.gov.pi.tce.siscap.api.service.ocr.PDFBoxImages;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ImagemParaTextoTest {

	@Test
	public void devemExistirConteudoRetornado() throws IOException {
		Map<Integer, byte[]> mapaImagens = extrairPaginasDoArquivo("diario oficial parnaiba 2.pdf");
		String texto = converterEmTexto(mapaImagens.get(0));
		
		System.out.println(texto);

		assertTrue(texto.length() > 0);
	}

	private Map<Integer, byte[]> extrairPaginasDoArquivo(String filename) throws IOException, FileNotFoundException {
		File diario = new ClassPathResource(filename).getFile();
		InputStream in = new FileInputStream(diario);
		
		PDF2Images pdfParaImagem = new PDFBoxImages();
		byte[] conteudo = IOUtils.toByteArray(in);
		Map<Integer, byte[]> mapaImagens = pdfParaImagem.convertToPages(conteudo);
		return mapaImagens;
	}

	private String converterEmTexto(byte[] conteudo) {
		Image2Text image2Text = new GoogleVisionImage2Text();
		return image2Text.convertToText(conteudo);
	}

}

