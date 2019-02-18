package br.gov.pi.tce.siscap.timer.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.pi.tce.siscap.timer.client.FeriadoServiceClient;
import br.gov.pi.tce.siscap.timer.model.Feriado;

@Service
public class FeriadoService {

	@Autowired
	private FeriadoServiceClient feriadoServiceClient;
	
	public Boolean isFeriado(LocalDate date, Long idFonte) {
		List<Feriado> feriados = feriadoServiceClient.feriadosPorFontePeriodo(idFonte, 	date, date);
		return (feriados != null && !(feriados.isEmpty()));
	}
}
