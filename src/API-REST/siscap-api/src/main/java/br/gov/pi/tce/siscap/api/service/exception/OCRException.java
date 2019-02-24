package br.gov.pi.tce.siscap.api.service.exception;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OCRException extends RuntimeException {
	private static final Logger logger = LoggerFactory.getLogger(OCRException.class);

	private static final long serialVersionUID = 1L;
	
	public OCRException(String msg) {
		super(msg);
		logger.error(msg);
	}

	public OCRException(String msg, IOException e) {
		super(msg, e);
		logger.error(msg, e);
	}

}
