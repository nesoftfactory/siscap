package br.gov.pi.tce.siscap.api.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.gov.pi.tce.siscap.api.model.TipoFonte;
import br.gov.pi.tce.siscap.api.repository.TipoFonteRepository;
import br.gov.pi.tce.siscap.api.service.exception.TipoFonteComNomeJaExistenteException;

@Service
public class TipoFonteService {

	
	@Autowired
	private TipoFonteRepository tipoFonteRepository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	public TipoFonte atualizar(Long id, TipoFonte tipoFonte) {
		TipoFonte tipoFonteSalva = buscarTipoFontePeloCodigo(id);
		BeanUtils.copyProperties(tipoFonte, tipoFonteSalva, "id", "dataCriacao", "usuarioCriacao");
		atualizarDadosEdicao(tipoFonteSalva);
		validarNomeTipoFonteDuplicado(tipoFonteSalva);
		tipoFonteRepository.save(tipoFonteSalva);
		
		return tipoFonteSalva;
	}
	

	public TipoFonte adicionar(TipoFonte tipoFonte) {
		atualizaDadosAdicao(tipoFonte);
		validarNomeTipoFonteDuplicado(tipoFonte);
		TipoFonte tipoFonteSalva = tipoFonteRepository.save(tipoFonte);
		
		return tipoFonteSalva;
	}

	private void validarNomeTipoFonteDuplicado(TipoFonte tipoFonte) {
		if (tipoFonte.isAlterando()) {
			List<TipoFonte> tiposFontes = tipoFonteRepository
					.buscarPorNomeComIdDiferenteDoInformado(tipoFonte.getNome(), tipoFonte.getId());
			if (!tiposFontes.isEmpty()) {
				throw new TipoFonteComNomeJaExistenteException();
			}
		} else {
			if(tipoFonteRepository.findByNome(tipoFonte.getNome()).isPresent()) {
				throw new TipoFonteComNomeJaExistenteException();
			}
		}
	}
	
	
	public void atualizarPropriedadeAtivo(Long id, Boolean ativo) {
		TipoFonte tipoFonte = buscarTipoFontePeloCodigo(id);
		tipoFonte.setAtivo(ativo);
		atualizarDadosEdicao(tipoFonte);
		tipoFonteRepository.save(tipoFonte);
	}
	
	private void atualizaDadosAdicao(TipoFonte tipoFonte) {
		tipoFonte.setUsuarioCriacao(usuarioService.getUsuarioLogado());
		atualizarDadosEdicao(tipoFonte);
	}

	private void atualizarDadosEdicao(TipoFonte tipoFonte) {
		tipoFonte.setUsuarioAtualizacao(usuarioService.getUsuarioLogado());
	}

	private TipoFonte buscarTipoFontePeloCodigo(Long id) {
		TipoFonte tipoFonteSalva = tipoFonteRepository.findById(id).
				orElseThrow(() -> new EmptyResultDataAccessException(1));
		return tipoFonteSalva;
	}
}
