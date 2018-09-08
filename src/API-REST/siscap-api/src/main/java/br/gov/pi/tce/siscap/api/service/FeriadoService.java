package br.gov.pi.tce.siscap.api.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.gov.pi.tce.siscap.api.model.Feriado;
import br.gov.pi.tce.siscap.api.model.Fonte;
import br.gov.pi.tce.siscap.api.repository.FeriadoRepository;
import br.gov.pi.tce.siscap.api.repository.FonteRepository;
import br.gov.pi.tce.siscap.api.service.exception.FonteInexistenteOuInativaException;

@Service
public class FeriadoService {

	private final String USUARIO = "automatizar";
	
	@Autowired
	private FonteRepository fonteRepository;
	
	@Autowired
	private FeriadoRepository feriadoRepository;
	
	public Feriado atualizar(Long id, Feriado feriado) {
		Feriado feriadoSalvo = buscarFeriadoPeloCodigo(id);
		BeanUtils.copyProperties(feriado, feriadoSalvo, "id", "dataCriacao", "usuarioCriacao");
		salvar(feriadoSalvo);
		
		return feriadoSalvo;
	}

	public Feriado adicionar(Feriado feriado) {
		atualizaDadosAdicao(feriado);
		return salvar(feriado);
	}

	public void atualizarPropriedadeAtivo(Long id, Boolean ativo) {
		Feriado feriado = buscarFeriadoPeloCodigo(id);
		feriado.setAtivo(ativo);
		salvar(feriado);
	}
	
	private Feriado salvar(Feriado feriadoSalvo) {
		validarFonte(feriadoSalvo);
		atualizarDadosEdicao(feriadoSalvo);
		
		return feriadoRepository.save(feriadoSalvo);
	}
	
	private void validarFonte(Feriado feriado) {
		if (feriado.getFonte() != null && feriado.getFonte().getId() != null) {
			Optional<Fonte> fonteOptional = fonteRepository.findById(feriado.getFonte().getId());

			if (!fonteOptional.isPresent() || fonteOptional.get().isInativo()) {
				throw new FonteInexistenteOuInativaException();
			}
		}
	}

	private void atualizaDadosAdicao(Feriado feriado) {
		feriado.setUsuarioCriacao(USUARIO);
	}

	private void atualizarDadosEdicao(Feriado feriado) {
		feriado.setUsuarioAtualizacao(USUARIO);
	}

	private Feriado buscarFeriadoPeloCodigo(Long id) {
		Feriado feriadoSalvo = feriadoRepository.findById(id).
				orElseThrow(() -> new EmptyResultDataAccessException(1));
		return feriadoSalvo;
	}
}
