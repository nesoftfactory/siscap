package br.gov.pi.tce.siscap.api.ocr;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
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

import br.gov.pi.tce.siscap.api.ocr.PDF2Images;
import br.gov.pi.tce.siscap.api.ocr.PDFBoxImages;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class PdfParaImagemTest {

	@Test
	public void devemExistirOitoPaginasEmPdf() throws IOException {
		int numeroPaginasEsperado = 8;
		File diario = new ClassPathResource(
			      "diario oficial parnaiba 2.pdf").getFile();
		InputStream in = new FileInputStream(diario);
		
		PDF2Images pdfParaImagem = new PDFBoxImages();
		byte[] conteudo = IOUtils.toByteArray(in);
		Map<Integer, byte[]> mapaImagens = pdfParaImagem.convertToPages(conteudo);
		//salvarEmArquivo(mapaImagens.get(0));
		
		assertEquals(numeroPaginasEsperado, mapaImagens.size());
	}
/*
	private void salvarEmArquivo(byte[] imagem) {
		File file = new File("pagina0.png");
		try (FileOutputStream fop = new FileOutputStream(file)) {
			fop.write(imagem);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
*/
}

