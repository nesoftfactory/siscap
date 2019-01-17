package br.gov.pi.tce.siscap.api.config.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

public class SiscapTokenEnhancer implements TokenEnhancer {

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
//		UsuarioSistema usuarioSistema = (UsuarioSistema) authentication.getPrincipal();
		OAuth2Authentication principal = (OAuth2Authentication) authentication.getPrincipal();
		
		final Map<String, Object> additionalInfo = new HashMap<>();
/*		additionalInfo.put("login", usuarioSistema.getUsuario().getEmail());
		additionalInfo.put("usuario_nome", usuarioSistema.getUsuario().getNome());
		additionalInfo.put("usuario_ativo", usuarioSistema.getUsuario().getAtivo());
		additionalInfo.put("admin_geral", usuarioSistema.getUsuario().isAdminGeral());
		additionalInfo.put("admin_orgao", usuarioSistema.getUsuario().isAdminOrgao());
		additionalInfo.put("orgao", usuarioSistema.getUsuario().getOrgao().getId());
		additionalInfo.put("orgao_nome", usuarioSistema.getUsuario().getOrgao().getNome());
*/

		additionalInfo.put("usuario_nome", principal.getName());
		additionalInfo.put("isAdmin", ("admin".equals(principal.getName()) ? true : false));

		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        
        return accessToken;
	}

}
