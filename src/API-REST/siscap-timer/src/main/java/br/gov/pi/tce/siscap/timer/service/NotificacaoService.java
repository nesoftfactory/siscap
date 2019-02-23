package br.gov.pi.tce.siscap.timer.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.pi.tce.siscap.timer.client.NotificacaoClient;
import br.gov.pi.tce.siscap.timer.client.NotificacaoConfigClient;
import br.gov.pi.tce.siscap.timer.mail.Mailer;
import br.gov.pi.tce.siscap.timer.model.Notificacao;
import br.gov.pi.tce.siscap.timer.model.NotificacaoConfig;
import br.gov.pi.tce.siscap.timer.model.Publicacao;
import br.gov.pi.tce.siscap.timer.model.Usuario;
import br.gov.pi.tce.siscap.timer.model.dto.NotificacaoDTO;
import br.gov.pi.tce.siscap.timer.model.enums.NotificacaoTipo;

@Service
public class NotificacaoService {

	@Autowired
	private NotificacaoClient notificacaoClient;
	
	@Autowired
	private NotificacaoConfigClient notificacaoConfigClient;
	
	@Autowired
	private Mailer mailer;

	public Notificacao checarNotificacao(Publicacao publicacaoRetorno) {
		List<NotificacaoConfig> notificacaoConfigList = notificacaoConfigClient
				.consultarNotificacaoConfigPorTipo(NotificacaoTipo.CAPTURA, true);
		Set<Usuario> usuariosNotificar = recuperarUsuariosNotificar(notificacaoConfigList,
				publicacaoRetorno.getQuantidadeTentativas());
		
		if (usuariosNotificar == null || usuariosNotificar.isEmpty())
			return null;
		
		String tituloNotificacao = "Notificação Coletor - DO não coletado";
		String conteudoNotificacao = "Não foi coletado o Diário Oficial da Fonte "
				+ publicacaoRetorno.getFonte().getNome() + " referente ao dia "
				+ publicacaoRetorno.getData();
		NotificacaoDTO notificacao = new NotificacaoDTO(NotificacaoTipo.CAPTURA.toString(),
				new ArrayList<>(usuariosNotificar), publicacaoRetorno.getId(), conteudoNotificacao);

		Notificacao notificacaoSalva = notificacaoClient.cadastrarNotificacao(notificacao);
		
		mailer.avisarEmailNotificacao(tituloNotificacao, conteudoNotificacao, usuariosNotificar);

		return notificacaoSalva;
	}

	public Notificacao checarNotificacaoOCR(Publicacao publicacao) {
		List<NotificacaoConfig> notificacaoConfigList = notificacaoConfigClient
				.consultarNotificacaoConfigPorTipo(NotificacaoTipo.OCR, true);
		Set<Usuario> usuariosNotificar = recuperarUsuariosNotificar(notificacaoConfigList,
				publicacao.getQuantidadeTentativasOCR());
		
		if (usuariosNotificar == null || usuariosNotificar.isEmpty())
			return null;
		
		String tituloNotificacao = "Notificação Coletor - OCR não coletado";
		String conteudoNotificacao = "Não foi realizado OCR do Diário Oficial da Fonte "
				+ publicacao.getFonte().getNome() + " referente ao dia "
				+ publicacao.getData();
		NotificacaoDTO notificacao = new NotificacaoDTO(NotificacaoTipo.CAPTURA.toString(),
				new ArrayList<>(usuariosNotificar), publicacao.getId(), conteudoNotificacao);

		Notificacao notificacaoSalva = notificacaoClient.cadastrarNotificacao(notificacao);
		
		mailer.avisarEmailNotificacao(tituloNotificacao, conteudoNotificacao, usuariosNotificar);

		return notificacaoSalva;
	}

	private HashSet<Usuario> recuperarUsuariosNotificar(List<NotificacaoConfig> notificacaoConfigList, 
			Long quantidadeTentativas) {
		if (notificacaoConfigList == null) {
			return null;
		}

		HashSet<Usuario> usuarios = new HashSet<>();
		for (NotificacaoConfig notificacaoConfig : notificacaoConfigList) {
			if (quantidadeTentativas > 0 && 
					(notificacaoConfig.getQuantidadeTentativas() == 0
						|| (quantidadeTentativas % notificacaoConfig.getQuantidadeTentativas()) == 0)
					) {

				usuarios.addAll(notificacaoConfig.getUsuarios());
			}
		}
		
		return usuarios;
	}

}
