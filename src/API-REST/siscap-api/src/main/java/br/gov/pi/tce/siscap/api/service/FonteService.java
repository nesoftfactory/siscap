package br.gov.pi.tce.siscap.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.gov.pi.tce.siscap.api.model.Fonte;
import br.gov.pi.tce.siscap.api.model.TipoFonte;
import br.gov.pi.tce.siscap.api.repository.FonteRepository;
import br.gov.pi.tce.siscap.api.repository.TipoFonteRepository;
import br.gov.pi.tce.siscap.api.service.exception.FonteComNomeJaExistenteException;
import br.gov.pi.tce.siscap.api.service.exception.TipoFonteInexistenteOuInativaException;

@Service
public class FonteService {

	
	@Autowired
	private FonteRepository fonteRepository;
	
	@Autowired
	private TipoFonteRepository tipoFonteRepository;
	
	@Autowired
	private UsuarioService usuarioService;
	
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
		validarNomeFonteDuplicado(fonte);
		validarTipoFonte(fonte);
		atualizarDadosEdicao(fonte);
		
		return fonteRepository.save(fonte);
	}
	
	private void validarNomeFonteDuplicado(Fonte fonte) {
		if (fonte.isAlterando()) {
			List<Fonte> fontes = fonteRepository
					.buscarPorNomeComIdDiferenteDoInformado(fonte.getNome(), fonte.getId());
			if (!fontes.isEmpty()) {
				throw new FonteComNomeJaExistenteException();
			}
		} else {
			if(fonteRepository.findByNome(fonte.getNome()).isPresent()) {
				throw new FonteComNomeJaExistenteException();
			}
		}
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
		fonte.setUsuarioCriacao(usuarioService.getUsuarioLogado());
	}

	private void atualizarDadosEdicao(Fonte fonte) {
		fonte.setUsuarioAtualizacao(usuarioService.getUsuarioLogado());
	}

	private Fonte buscarFontePeloCodigo(Long id) {
		Fonte fonteSalva = fonteRepository.findById(id).
				orElseThrow(() -> new EmptyResultDataAccessException(1));
		return fonteSalva;
	}
}
