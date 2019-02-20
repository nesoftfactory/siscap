package br.gov.pi.tce.siscap.timer.mail;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import br.gov.pi.tce.siscap.timer.model.Usuario;

@Component
public class Mailer {

	private final String REMETENTE = "siscap@tce.pi.gov.br";
	@Autowired
	private JavaMailSender mailSender;

	/*
	 * @EventListener() private void teste(ApplicationEvent event) {
	 * this.enviarEmail("teste.filho@gmail.com", Arrays.asList("amsf10@gmail.com"),
	 * "Testanto", "Olá!<br/>Teste ok.");
	 * System.out.println("Terminado o envio de e-mail"); }
	 */

	public void avisarEmailNotificacao(String tituloNotificacao, String conteudoNotificacao, Set<Usuario> usuarios) {
		enviarEmail(REMETENTE, destinatarios(usuarios), tituloNotificacao, conteudoNotificacao);
	}

	private List<String> destinatarios(Set<Usuario> usuarios) {
		List<String> emails = usuarios.stream()
			.map(u -> u.getEmail())
			.collect(Collectors.toList());
		return emails;
	}

	public void enviarEmail(String remetente, List<String> destinatarios, String assunto, String mensagem) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
			helper.setFrom(remetente);
			helper.setTo(destinatarios.toArray(new String[destinatarios.size()]));
			helper.setSubject(assunto);
			helper.setText(mensagem, true);

			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			throw new RuntimeException("Problemas com o envio de e-mail", e);
		}
	}

}
