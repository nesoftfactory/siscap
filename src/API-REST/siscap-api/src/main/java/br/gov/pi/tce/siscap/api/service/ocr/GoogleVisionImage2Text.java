package br.gov.pi.tce.siscap.api.service.ocr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.TextAnnotation;
import com.google.protobuf.ByteString;

import br.gov.pi.tce.siscap.api.service.exception.OCRException;

@Component
public class GoogleVisionImage2Text implements Image2Text {

	@Override
	public String convertToText(byte[] conteudo) {
		StringBuffer textoCompletoConvertido = new StringBuffer();

		try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {
			
			ByteString imgBytes = ByteString.copyFrom(conteudo);

			// Builds the image annotation request
			List<AnnotateImageRequest> requests = new ArrayList<>();
			Image img = Image.newBuilder().setContent(imgBytes).build();
			Feature feat = Feature.newBuilder().setType(Type.DOCUMENT_TEXT_DETECTION).build();
			AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
			requests.add(request);

			// For full list of available annotations, see http://g.co/cloud/vision/docs
			BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
			List<AnnotateImageResponse> responses = response.getResponsesList();

			for (AnnotateImageResponse res : responses) {
				if (res.hasError()) {
					throw new OCRException("Erro ao fazer OCR: " + res.getError().getMessage());		
				}

				// For full list of available annotations, see http://g.co/cloud/vision/docs
				TextAnnotation annotation = res.getFullTextAnnotation();
				textoCompletoConvertido.append(annotation.getText());
				textoCompletoConvertido.append(System.lineSeparator());
			}
		} catch (IOException e) {
			throw new OCRException("Erro ao fazer OCR");		
		}

		return textoCompletoConvertido.toString();
	}

}
