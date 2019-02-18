package br.gov.pi.tce.siscap.timer.collect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.gov.pi.tce.siscap.timer.model.Fonte;
import br.gov.pi.tce.siscap.timer.service.ColetorService;
import br.gov.pi.tce.siscap.timer.util.DateUtil;

@Service
public class PiauiColetor implements Coletor {
	private static final Logger logger = LoggerFactory.getLogger(ColetorService.class);

	private final String URL_COLETA = "http://www.diariooficial.pi.gov.br/diario.php?dia="; 
	private final String URL_DOWNLOAD = "http://www.diariooficial.pi.gov.br/diario/";
	private final String REGEX_FOR_PDF = "[0-9]+[/]+DIARIO+[0-9]+[_]+[0-9A-Za-z]+[.][Pp][Dd][Ff]";

	private ColetorService coletorService;

	@Override
	public void coletar(ColetorService coletorService, Fonte fonte, 
			Date dataInicial, Date dataFinal) {
		this.coletorService = coletorService;
		
		List<LocalDate> localDateList = DateUtil.getDiasUteis(dataInicial, dataFinal);
		for (LocalDate localDate : localDateList) {

			try {
				String linhaHTML;
				Date date = DateUtil.convertToDate(localDate);
				Calendar c = Calendar.getInstance();
				c.setTime(date);
				String dia = String.format("%02d", c.get(Calendar.DATE));
				String mes = String.format("%02d", c.get(Calendar.MONTH) + 1); // janeiro = 0
				String ano = String.valueOf(c.get(Calendar.YEAR));
				Matcher matcher = null;

				// Constrói nova url para coletar o PDF e Busca em nova fonte
				URL url = new URL(URL_COLETA + ano + mes + dia);
				BufferedReader fonteHTML = new BufferedReader(new InputStreamReader(url.openStream()));

				buscaPDF: while ((linhaHTML = fonteHTML.readLine()) != null) {
					Pattern pdfPattern = Pattern.compile(REGEX_FOR_PDF);

					matcher = pdfPattern.matcher(linhaHTML);
					while (matcher.find()) {
						if (date != null) {
							String localPublicacao[] = matcher.group().split("/");
							
							this.coletorService.salvarPublicacao(fonte, URL_DOWNLOAD + matcher.group(),
									date, localPublicacao[1], Boolean.TRUE, 
									Boolean.FALSE, "Sucesso", null, null, "", localPublicacao[1]);

							// Ao encontrar o pdf sai do loop mais externo
							break buscaPDF;
						}
					}
				}
				coletorService.checarPublicacaoInexistente(Arrays.asList(localDate), fonte);
				fonteHTML.close();

			} catch (MalformedURLException excecao) {
				logger.error(excecao.getMessage());
			} catch (IOException excecao) {
				logger.error(excecao.getMessage());
			}
		}
	}	

}