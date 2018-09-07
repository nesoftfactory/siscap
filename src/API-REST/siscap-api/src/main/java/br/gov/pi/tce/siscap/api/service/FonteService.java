package br.gov.pi.tce.siscap.api.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.gov.pi.tce.siscap.api.model.Fonte;
import br.gov.pi.tce.siscap.api.model.TipoFonte;
import br.gov.pi.tce.siscap.api.repository.FonteRepository;
import br.gov.pi.tce.siscap.api.repository.TipoFonteRepository;
import br.gov.pi.tce.siscap.api.service.exception.TipoFonteInexistenteOuInativaException;

@Service
public class FonteService {

	private final String USUARIO = "automatizar";
	
	@Autowired
	private FonteRepository fonteRepository;
	
	@Autowired
	private TipoFonteRepository tipoFonteRepository;
	
	public Fonte atualizar(Long id, Fonte fonte) {
		Fonte fonteSalva = buscarFontePeloCodigo(id);
		BeanUtils.copyProperties(fonte, fonteSalva, "id", "dataCriacao", "usuarioCriacao");
		salvar(fonteSalva);
		
		return fonteSalva;
	}

	public Fonte adicionar(Fonte fonte) {
		atualizaDadosAdicao(fonte);
		return salvar(fonte);
	}

	public void atualizarPropriedadeAtivo(Long id, Boolean ativo) {
		Fonte fonte = buscarFontePeloCodigo(id);
		fonte.setAtivo(ativo);
		salvar(fonte);
	}
	
	private Fonte salvar(Fonte fonte) {
		validarTipoFonte(fonte);
		atualizarDadosEdicao(fonte);
		
		return fonteRepository.save(fonte);
	}
	
	private void validarTipoFonte(Fonte fonte) {
		Optional<TipoFonte> tipoFonteOptional = null;
		if (fonte.getTipoFonte().getId() != null) {
			tipoFonteOptional = tipoFonteRepository.findById(fonte.getTipoFonte().getId());
		}

		if (!tipoFonteOptional.isPresent() || tipoFonteOptional.get().isInativo()) {
			throw new TipoFonteInexistenteOuInativaException();
		}
	}

	private void atualizaDadosAdicao(Fonte fonte) {
		fonte.setUsuarioCriacao(USUARIO);
	}

	private void atualizarDadosEdicao(Fonte fonte) {
		fonte.setUsuarioAtualizacao(USUARIO);
	}

	private Fonte buscarFontePeloCodigo(Long id) {
		Fonte fonteSalva = fonteRepository.findById(id).
				orElseThrow(() -> new EmptyResultDataAccessException(1));
		return fonteSalva;
	}
}
