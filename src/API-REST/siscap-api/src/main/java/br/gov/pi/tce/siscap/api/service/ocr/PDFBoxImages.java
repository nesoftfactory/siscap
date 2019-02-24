package br.gov.pi.tce.siscap.api.service.ocr;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import br.gov.pi.tce.siscap.api.service.exception.OCRException;

@Component
public class PDFBoxImages implements PDF2Images {
	private static final Logger logger = LoggerFactory.getLogger(PDFBoxImages.class);

	@Override
	public Map<Integer, byte[]> convertToPages(byte[] conteudo) {
		
		Map<Integer, byte[]> mapaImagens = new HashMap<>();
		try (PDDocument document = PDDocument.load(conteudo)){

			/*
			PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();
			PDTextField field = (PDTextField) acroForm.getField("q2_quotationPrepared");
			COSName helvName = acroForm.getDefaultResources().add(PDType1Font.HELVETICA); // use different font if you want. Do not subset!
			field.setDefaultAppearance("/" + helvName.getName() + " 10 Tf 0 g"); // modifies your existing DA string
			field.setValue("TextEntry"); 
			*/
			
			System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
			PDPageTree pages = document.getDocumentCatalog().getPages();
			
			PDFRenderer pdfRenderer = new PDFRenderer(document);
			for (int pageCounter = 0; pageCounter < pages.getCount(); pageCounter++) {
				logger.info("Convertendo em imagem página " + (pageCounter + 1) + " de " + pages.getCount());

				BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(pageCounter, 300, ImageType.RGB);
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				//ImageIOUtil.writeImage(bufferedImage, "pagina-" + (pageCounter++) + ".png", 300);
				ImageIOUtil.writeImage(bufferedImage, "PNG", os, 300);
				mapaImagens.put(pageCounter, os.toByteArray());
			}
		} catch (IOException e) {
			throw new OCRException("Não foi possível ler o conteúdo do arquivo", e);		
		}

		return mapaImagens;
	}
}
