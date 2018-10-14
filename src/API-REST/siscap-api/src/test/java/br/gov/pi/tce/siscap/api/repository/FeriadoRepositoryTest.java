package br.gov.pi.tce.siscap.api.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.gov.pi.tce.siscap.api.model.Feriado;
import br.gov.pi.tce.siscap.api.model.Fonte;
import br.gov.pi.tce.siscap.api.model.TipoFonte;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FeriadoRepositoryTest {

	@Autowired
	private FeriadoRepository feriadoRepository;
	
	@Autowired
	private FonteRepository fonteRepository;
	
	@Autowired
	private TipoFonteRepository tipoFonteRepository;
	
	private TipoFonte tipoFonte;
	private Fonte fonte1;
	private Fonte fonte2;
	private Feriado somenteFonte1_2018_10_19;
	
	@Before
	public void setUp() {
		criarTipoFonte();
		criarFontes();
		criarFeriaodos();
	}

	private void criarTipoFonte() {
		tipoFonte = new TipoFonte();
		tipoFonte.setNome("TipoFonte");
		tipoFonte.setAtivo(true);
		tipoFonte = tipoFonteRepository.save(tipoFonte);
	}

	@After
	public void tearDown() {
		feriadoRepository.deleteAll();
		fonteRepository.deleteAll();
	}
	
	@Test
	public void deveEnconcontarFeriadoFixoAnoDiferenteCadastrado() {
		LocalDate data = LocalDate.parse("2010-09-07", DateTimeFormatter.ISO_LOCAL_DATE);
		List<Long> resultado = feriadoRepository.buscarPorDataEFonte(data, fonte1.getId());
		
		assertFalse(resultado.isEmpty());
	}

	@Test
	public void deveEncontrarFeriadoFontePresenteLista() {
		LocalDate data = somenteFonte1_2018_10_19.getData();
		List<Long> resultado = feriadoRepository.buscarPorDataEFonte(data, fonte1.getId());
		
		assertFalse(resultado.isEmpty());
	}

	@Test
	public void naoDeveEncontrarFeriadoFonteNaoPresenteLista() {
		LocalDate data = somenteFonte1_2018_10_19.getData();
		List<Long> resultado = feriadoRepository.buscarPorDataEFonte(data, fonte2.getId());
		
		assertTrue(resultado.isEmpty());
	}

	private void criarFeriaodos() {
		criarFeriado(LocalDate.parse("2018-09-07", DateTimeFormatter.ISO_LOCAL_DATE),
				"Fixo e todas fontes", true, true, null);
		somenteFonte1_2018_10_19 = criarFeriado(LocalDate.parse("2018-10-19", DateTimeFormatter.ISO_LOCAL_DATE),
				"Somente Fonte1", false, false, Arrays.asList(fonte1));
	}

	private Feriado criarFeriado(LocalDate data, String nome, boolean fixo, boolean todasFontes, List<Fonte> fontes) {
		Feriado feriado = new Feriado();
		feriado.setData(data);
		feriado.setNome(nome);
		feriado.setAtivo(true);
		feriado.setFixo(fixo);
		feriado.setTodasFontes(todasFontes);
		feriado.setFontes(fontes);
		feriado = feriadoRepository.save(feriado);
		return feriado;
	}

	private void criarFontes() {
		fonte1 = criarFonte("Fonte 1");
		fonte2 = criarFonte("Fonte 2");		
	}

	private Fonte criarFonte(String nome) {
		Fonte fonte = new Fonte();
		fonte.setTipoFonte(tipoFonte);
		fonte.setNome(nome);
		fonte.setAtivo(true);
		fonte = fonteRepository.save(fonte);
		return fonte;
	}

}

