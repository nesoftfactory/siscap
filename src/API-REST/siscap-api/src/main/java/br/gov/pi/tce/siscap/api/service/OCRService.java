package br.gov.pi.tce.siscap.api.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.pi.tce.siscap.api.model.Arquivo;
import br.gov.pi.tce.siscap.api.model.PaginaOCRArquivo;
import br.gov.pi.tce.siscap.api.repository.ArquivoRepository;
import br.gov.pi.tce.siscap.api.service.exception.OCRException;
import br.gov.pi.tce.siscap.api.service.ocr.Image2Text;
import br.gov.pi.tce.siscap.api.service.ocr.PDF2Images;

@Service
public class OCRService {

	@Autowired
	private Image2Text image2Text;
	
	@Autowired
	private PDF2Images pdf2Images;
	
	@Autowired
	private ArquivoRepository arquivoRepository;

	public Map<Integer, PaginaOCRArquivo> getOCRPaginasArquivo(Long idArquivo) {
		Map<Integer, PaginaOCRArquivo> mapaPaginasArquivo = new HashMap<>();

		Arquivo arquivo = buscarArquivoPorId(idArquivo);
		
		Map<Integer, byte[]> pages = pdf2Images.convertToPages(arquivo.getConteudo());
		for (Map.Entry<Integer, byte[]> pair : pages.entrySet()) {
			byte[] conteudoPagina = pair.getValue();
			String textoPagina = image2Text.convertToText(conteudoPagina);
			mapaPaginasArquivo.put(pair.getKey(), new PaginaOCRArquivo(pair.getKey() + 1 , textoPagina, arquivo));
		}
		return mapaPaginasArquivo;
	}

	private Arquivo buscarArquivoPorId(Long idArquivo) {
		Optional<Arquivo> arquivoOptional = arquivoRepository.findById(idArquivo);
		Arquivo arquivo = arquivoOptional.isPresent() ? arquivoOptional.get() : null;
		if(arquivo == null) {
			throw new OCRException("Erro ao realizar OCR do arquivo: " + idArquivo + ". O arquivo não foi encontrado");
		}
		return arquivo;
	}
}
