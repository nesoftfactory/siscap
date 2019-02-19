package br.gov.pi.tce.siscap.timer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.pi.tce.siscap.timer.client.NotificacaoClient;
import br.gov.pi.tce.siscap.timer.mail.Mailer;
import br.gov.pi.tce.siscap.timer.model.Notificacao;
import br.gov.pi.tce.siscap.timer.model.NotificacaoConfig;
import br.gov.pi.tce.siscap.timer.model.Publicacao;
import br.gov.pi.tce.siscap.timer.model.enums.NotificacaoTipo;

@Service
public class NotificacaoService {

	@Autowired
	private NotificacaoClient notificacaoClient;
	
	@Autowired
	private Mailer mailer;

	public Notificacao cadastrarNotificacao(Publicacao publicacaoRetorno) {
		List<NotificacaoConfig> notificacaoConfigList = notificacaoClient
				.consultarNotificacaoConfigPorTipo(NotificacaoTipo.CAPTURA, true); 
		Publicacao publicacaoNotif = new Publicacao(publicacaoRetorno.getId());
		String tituloNotificacao = "Notificação Coletor - Não foi coletado o Diário Oficial da Fonte "
				+ publicacaoRetorno.getFonte().getNome() + " referente ao dia "
				+ publicacaoRetorno.getData();
		String conteudoNotificacao = "Não foi coletado o Diário Oficial da Fonte "
				+ publicacaoRetorno.getFonte().getNome() + " referente ao dia "
				+ publicacaoRetorno.getData();
		Notificacao notificacao = new Notificacao(NotificacaoTipo.CAPTURA,
				notificacaoConfigList.get(0).getUsuarios(), publicacaoNotif, conteudoNotificacao);

		Notificacao notificacaoSalva = notificacaoClient.cadastrarNotificacao(notificacao);
		
		mailer.avisarEmailNotificacao(tituloNotificacao, conteudoNotificacao, 
				notificacaoConfigList.get(0).getUsuarios());

		return notificacaoSalva;
	}

}
