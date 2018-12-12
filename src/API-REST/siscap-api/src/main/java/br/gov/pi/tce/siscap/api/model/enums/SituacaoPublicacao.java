package br.gov.pi.tce.siscap.api.model.enums;

public enum SituacaoPublicacao {
	COLETA_REALIZADA("Coleta Realizada"), OCR_REALIZADO("OCR Realizado"), INDEXACAO_REALIZADA("Indexação Realizada");
	
	
	private String descricao;
	
	
	private SituacaoPublicacao(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
}
