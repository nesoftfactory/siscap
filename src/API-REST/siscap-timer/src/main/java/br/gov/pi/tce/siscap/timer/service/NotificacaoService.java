package br.gov.pi.tce.siscap.timer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.pi.tce.siscap.timer.client.NotificacaoClient;
import br.gov.pi.tce.siscap.timer.mail.Mailer;
import br.gov.pi.tce.siscap.timer.model.Notificacao;

@Service
public class NotificacaoService {

	@Autowired
	private NotificacaoClient notificacaoClient;
	
	@Autowired
	private Mailer mailer;
	
	public Notificacao cadastrarNotificacao(Notificacao notificacao) {
		/*
		List<NotificacaoConfig> notificacaoConfigList = consultarNotificacaoConfigPorTipoAtivo(
				NotificacaoTipo.CAPTURA, Boolean.TRUE);
		PublicacaoN publicacaoNotif = new PublicacaoN(publicacaoRetorno.getId());
		String tituloNotificacao = propriedades.getValorString("EMAIL_SUBJECT")
				+ publicacaoRetorno.getFonte().getNome() + propriedades.getValorString("EMAIL_SUBJECT_2")
				+ publicacaoRetorno.getData();
		String conteudoNotificacao = propriedades.getValorString("EMAIL_CONTENT")
				+ publicacaoRetorno.getFonte().getNome() + propriedades.getValorString("EMAIL_CONTENT_2")
				+ publicacaoRetorno.getData();
		NotificacaoN notificacaoN = new NotificacaoN(NotificacaoTipo.CAPTURA,
				notificacaoConfigList.get(0).getUsuarios(), publicacaoNotif, conteudoNotificacao);
		cadastrarNotificacao(notificacaoN);
		notificacao.sendEmail(propriedades.getValorString("EMAIL_TO"),
				propriedades.getValorString("EMAIL_FROM"), tituloNotificacao, conteudoNotificacao);

		//Notificacao notificacaoCadastrada = notificacaoClient.cadastrarNotificacao(notificacao);
		
		//TODO enviar e-mails para usuarios com perfil adequado
		//mailer.enviarEmail(remetente, destinatarios, assunto, mensagem);
		
		return notificacaoCadastrada;
		*/
		return null;
	}
}
