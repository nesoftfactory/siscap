package br.gov.pi.tce.siscap.api.ocr;

import java.util.Map;

public interface PDF2Images {

	Map<Integer, byte[]> convertToPages(byte[] conteudo);

}