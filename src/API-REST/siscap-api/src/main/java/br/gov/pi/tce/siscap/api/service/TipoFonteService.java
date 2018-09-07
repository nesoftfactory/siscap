package br.gov.pi.tce.siscap.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.gov.pi.tce.siscap.api.model.TipoFonte;
import br.gov.pi.tce.siscap.api.repository.TipoFonteRepository;

@Service
public class TipoFonteService {

	private final String USUARIO = "automatizar";
	
	@Autowired
	private TipoFonteRepository tipoFonteRepository;
	
	public TipoFonte atualizar(Long id, TipoFonte tipoFonte) {
		TipoFonte tipoFonteSalva = buscarTipoFontePeloCodigo(id);
		BeanUtils.copyProperties(tipoFonte, tipoFonteSalva, "id", "dataCriacao", "usuarioCriacao");
		atualizarDadosEdicao(tipoFonteSalva);
		tipoFonteRepository.save(tipoFonteSalva);
		
		return tipoFonteSalva;
	}

	public TipoFonte adicionar(TipoFonte tipoFonte) {
		atualizaDadosAdicao(tipoFonte);
		TipoFonte tipoFonteSalva = tipoFonteRepository.save(tipoFonte);
		
		return tipoFonteSalva;
	}

	public void atualizarPropriedadeAtivo(Long id, Boolean ativo) {
		TipoFonte tipoFonte = buscarTipoFontePeloCodigo(id);
		tipoFonte.setAtivo(ativo);
		atualizarDadosEdicao(tipoFonte);
		tipoFonteRepository.save(tipoFonte);
	}
	
	private void atualizaDadosAdicao(TipoFonte tipoFonte) {
		tipoFonte.setUsuarioCriacao(USUARIO);
		atualizarDadosEdicao(tipoFonte);
	}

	private void atualizarDadosEdicao(TipoFonte tipoFonte) {
		tipoFonte.setUsuarioAtualizacao(USUARIO);
	}

	private TipoFonte buscarTipoFontePeloCodigo(Long id) {
		TipoFonte tipoFonteSalva = tipoFonteRepository.findById(id).
				orElseThrow(() -> new EmptyResultDataAccessException(1));
		return tipoFonteSalva;
	}
}
