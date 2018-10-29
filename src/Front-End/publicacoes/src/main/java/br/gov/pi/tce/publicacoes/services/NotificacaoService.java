package br.gov.pi.tce.publicacoes.services;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import br.gov.pi.tce.publicacoes.util.Propriedades;

/**
 * Classe responsável por enviar as notificacoes das coletas das publicações nos sites.
 * 
 * 
 * @author Erick Guilherme Cavalcanti
 *
 */
@Stateless
public class NotificacaoService {
	
	private static final Logger LOGGER = Logger.getLogger(NotificacaoService.class);

	//@Resource(lookup = "java:jboss/mail/publicacoes") // Nome do Recurso que criamos no Wildfly
	private Session mailSession; // Objeto que vai reprensentar uma sessão de email

	@Asynchronous // Metodo Assíncrono para que a aplicação continue normalmente sem ficar
					// bloqueada até que o email seja enviado
	public void sendEmail(String to, String from, String subject, String content) {

		LOGGER.info("Notificacao enviada por " + from + " para " + to + " : " + subject);
		try {
			// Criação de uma mensagem simples
			Message message = new MimeMessage(mailSession);
			// Cabeçalho do Email
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(subject);

			Propriedades propriedades = Propriedades.getInstance();
			
			// Corpo do email
			// message.setText(content);
			message.setContent(content, propriedades.getValorString("EMAIL_CONTENT_TYPE"));

			// Envio da mensagem
			Transport.send(message);
			LOGGER.info("Notificacao enviada");
		} catch (MessagingException e) {
			LOGGER.error("Erro ao enviar a notificacao: " + e.getMessage());
		}
	}
}
