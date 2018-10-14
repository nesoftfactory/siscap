package br.gov.pi.tce.siscap.api.converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToLocalDateConverter implements Converter<String, LocalDate> {

	@Override
	public LocalDate convert(String source) {
		return LocalDate.parse(source, DateTimeFormatter.ISO_LOCAL_DATE);
	}

}
