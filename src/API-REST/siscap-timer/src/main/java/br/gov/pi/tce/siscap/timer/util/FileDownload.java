package br.gov.pi.tce.siscap.timer.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;

import br.gov.pi.tce.siscap.timer.service.ColetorService;

public class FileDownload {
	private static final Logger logger = LoggerFactory.getLogger(ColetorService.class);
	private String linkArquivo;
	
	public FileDownload(String linkArquivo) {
		this.linkArquivo = linkArquivo;
	}


	public FileSystemResource realizarDownload() {
		logger.info("Realizando download de " + this.linkArquivo);

		URL url;
		File file = null;
		try {
			url = new URL(UriUtil.encodeString(this.linkArquivo));
			
			// TODO Bixar em memoria ou criar nome randomico
			file = new File(System.getProperty("java.io.tmpdir") + File.separator	+ "temp.pdf");
			FileUtils.copyURLToFile(url, file);

		} catch (MalformedURLException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		FileSystemResource fileSystemResource = new FileSystemResource(file);
		
		logger.info("Concluído o download de " + this.linkArquivo);
		return fileSystemResource;
	}
	
}
