package br.gov.pi.tce.siscap.api.ocr;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

import br.gov.pi.tce.siscap.api.service.exception.OCRException;

public class PDFBoxImages implements PDF2Images {

	@Override
	public Map<Integer, byte[]> convertToPages(byte[] conteudo) {
		Map<Integer, byte[]> mapaImagens = new HashMap<>();
		try (PDDocument document = PDDocument.load(conteudo)){
			PDPageTree pages = document.getDocumentCatalog().getPages();
			
			PDFRenderer pdfRenderer = new PDFRenderer(document);
			for (int pageCounter = 0; pageCounter < pages.getCount(); pageCounter++) {
				BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(pageCounter, 300, ImageType.RGB);
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				//ImageIOUtil.writeImage(bufferedImage, "pagina-" + (pageCounter++) + ".png", 300);
				ImageIOUtil.writeImage(bufferedImage, "PNG", os, 300);
				mapaImagens.put(pageCounter, os.toByteArray());
			}
		} catch (IOException e) {
			throw new OCRException("Não foi possível ler o conteúdo do arquivo");		
		}

		return mapaImagens;
	}
}
