package br.gov.pi.tce.siscap.timer.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.pi.tce.siscap.timer.client.PublicacaoServiceClient;
import br.gov.pi.tce.siscap.timer.config.property.SiscapTimerProperty;
import br.gov.pi.tce.siscap.timer.model.Arquivo;
import br.gov.pi.tce.siscap.timer.model.Fonte;
import br.gov.pi.tce.siscap.timer.model.Publicacao;
import br.gov.pi.tce.siscap.timer.model.PublicacaoAnexo;
import br.gov.pi.tce.siscap.timer.util.DateUtil;

@Service
public class ColetorService {
	private static final Logger logger = LoggerFactory.getLogger(ColetorService.class);
	
	@Autowired
	private PublicacaoServiceClient publicacaoServiceClient;
	
	@Autowired
	private FeriadoService feriadoService;

	@Autowired
	private NotificacaoService notificacaoService;
	
	@Autowired
	private SiscapTimerProperty property;
	
	public void checarPublicacaoInexistente(List<LocalDate> diasUteisList, Fonte fonte) {
		logger.info("checando publicacao inexistente");
		
		for (LocalDate date : diasUteisList) {
			Boolean isFeriado = feriadoService.isFeriado(date, fonte.getId());
			if (!isFeriado) {
				logger.info("Não foi coletado Diário da fonte " + fonte.getNome() +
						" referente ao dia " + DateUtil.convertLocalDateToString(date));
				salvarPublicacao(fonte, "", DateUtil.convertToDate(date), "", Boolean.FALSE, Boolean.FALSE,
						"Erro: Diário não encontrado", null, null, "",
						"Diário indisponível");
			}
		}
		
	}

	public void salvarPublicacao(Fonte fonte, String linkArquivoPublicacao, Date dataPublicacao,
			String nomeArquivoPublicacao, Boolean isSucesso, Boolean isAnexo, String mensagemErro,
			PublicacaoAnexo publicacaoAnexo, Arquivo arquivoAnexo, String codigo, String publicacaoName) {
		
		Arquivo arquivo = new Arquivo(nomeArquivoPublicacao, Long.valueOf(10), "tipo", linkArquivoPublicacao,
				"conteudo".getBytes());
		Publicacao publicacao = new Publicacao(fonte, publicacaoName, DateUtil.asLocalDate(dataPublicacao), codigo, arquivo.getId(),
				isSucesso, isAnexo, Long.valueOf(1));
		
		logger.info("INICIANDO METODO SALVAR PUBLICACAO - " + publicacao);

		Publicacao publicacaoConsultada = consultarPublicacaoPorFonteDataNomeArquivo(publicacao);
		if (publicacaoConsultada == null) {
			Publicacao publicacaoIndisponivel = new Publicacao(fonte,
					"Indisponível", DateUtil.asLocalDate(dataPublicacao), codigo,
					arquivo.getId(), isSucesso, isAnexo, Long.valueOf(1));
			Publicacao publicacaoConsultadaIndisponivel = consultarPublicacaoPorFonteDataNomeArquivo(
					publicacaoIndisponivel);
			if (publicacaoConsultadaIndisponivel != null) {
				publicacaoConsultada = publicacaoConsultadaIndisponivel;
			}
		}
		Publicacao publicacaoRetorno = publicacao;
		try {
			if (publicacaoConsultada == null) {
				publicacaoRetorno = publicacaoServiceClient.cadastrarPublicacao(publicacao, arquivo);
				// publicacaoRetorno = publicacao;
				if (publicacaoAnexo != null) {
					publicacaoAnexo.setPublicacao(publicacaoRetorno);
					publicacaoAnexo = publicacaoServiceClient.cadastrarPublicacaoAnexo(publicacaoAnexo, arquivoAnexo,
							false);
				}
			} else {
				if (!publicacaoConsultada.getSucesso()) {
					// se quantidade for igual a 3 ou mais - atualizar que não vai buscar mais
					// (status) e futuramente gerar notificacao
					publicacaoConsultada.setQuantidadeTentativas(publicacaoConsultada.getQuantidadeTentativas() + 1);
					publicacaoConsultada.setNome(publicacao.getNome());
					publicacaoConsultada.setFonte(publicacao.getFonte());
					publicacaoConsultada.setData(publicacao.getData());
					publicacaoConsultada.setCodigo(publicacao.getCodigo());
					publicacaoConsultada.setSucesso(publicacao.getSucesso());
					publicacaoConsultada.setPossuiAnexo(publicacao.getPossuiAnexo());
					publicacaoRetorno = publicacaoServiceClient.alterarPublicacao(publicacaoConsultada, arquivo, false);
					// publicacaoRetorno = publicacaoConsultada;
				}
			}
		} catch (Exception e) {
			logger.error("Erro ao Criar/Atualizar Publicacao.");
			logger.error(e.getMessage());
		}
		if (!isSucesso) {
			Long qtTentativasColetar = publicacaoRetorno.getQuantidadeTentativas();
			Long qtTentativasNotificar = new Long(property.getQuantidadeTentativasNofiticar());
			if (qtTentativasColetar > 0 && 
					(qtTentativasNotificar == 0
						|| (qtTentativasColetar	% qtTentativasNotificar) == 0)
					) {
				
				notificacaoService.cadastrarNotificacao(publicacaoRetorno);
			}

		}
	}

	private Publicacao consultarPublicacaoPorFonteDataNomeArquivo(Publicacao publicacao) {
		Publicacao publicacaoConsultada = null;
		List<Publicacao> publicacaolist = publicacaoServiceClient.consultarPublicacaoPorFonteDataNome(
				publicacao.getFonte().getId(), 
				publicacao.getData(),
				publicacao.getNome());
		if (publicacaolist != null && !publicacaolist.isEmpty()) {
			publicacaoConsultada = publicacaolist.get(0);
		}
		return publicacaoConsultada;
	}

}
