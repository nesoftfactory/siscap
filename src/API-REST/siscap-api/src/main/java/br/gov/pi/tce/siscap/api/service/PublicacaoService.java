package br.gov.pi.tce.siscap.api.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.gov.pi.tce.siscap.api.model.Arquivo;
import br.gov.pi.tce.siscap.api.model.Fonte;
import br.gov.pi.tce.siscap.api.model.Publicacao;
import br.gov.pi.tce.siscap.api.model.Usuario;
import br.gov.pi.tce.siscap.api.repository.FonteRepository;
import br.gov.pi.tce.siscap.api.repository.PublicacaoRepository;
import br.gov.pi.tce.siscap.api.service.exception.FonteInexistenteOuInativaException;

@Service
public class PublicacaoService {

	@Autowired
	private PublicacaoRepository publicacaoRepository;
	
	@Autowired
	private FonteRepository fonteRepository;
	
	@Autowired
	private UsuarioService usuarioService;

	public Publicacao adicionar(Publicacao publicacao, MultipartFile partFile) throws IOException {
		Arquivo arquivo = atualizarArquivo(partFile);
		if (arquivo != null) {
			publicacao.setArquivo(arquivo);
		}
		
		atualizaDadosAdicao(publicacao);
		return salvar(publicacao);
	}

	private Arquivo atualizarArquivo(MultipartFile partFile) throws IOException {
		if (partFile != null) {
			Arquivo arquivo = new Arquivo(partFile);
			Usuario usuarioLogado = usuarioService.getUsuarioLogado();
			arquivo.setUsuarioCriacao(usuarioLogado);
			arquivo.setUsuarioAtualizacao(usuarioLogado);
			
			return arquivo;
		}

		return null;
	}

	private Publicacao salvar(Publicacao publicacaoSalva) {
		validarFonte(publicacaoSalva);
		atualizarDadosEdicao(publicacaoSalva);
		
		return publicacaoRepository.save(publicacaoSalva);
	}

	private void atualizarDadosEdicao(Publicacao publicacaoSalva) {
		publicacaoSalva.setUsuarioAtualizacao(usuarioService.getUsuarioLogado());
	}

	private void validarFonte(Publicacao publicacaoSalva) {
		if (publicacaoSalva.getFonte() != null && publicacaoSalva.getFonte().getId() != null) {
			Optional<Fonte> fonteOptional = fonteRepository.findById(publicacaoSalva.getFonte().getId());

			if (!fonteOptional.isPresent() || fonteOptional.get().isInativo()) {
				throw new FonteInexistenteOuInativaException();
			}
		}
	}

	private void atualizaDadosAdicao(Publicacao publicacao) {
		publicacao.setUsuarioCriacao(usuarioService.getUsuarioLogado());
	}
}
