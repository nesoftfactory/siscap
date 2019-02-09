/**
 * 
 */
package br.gov.pi.tce.publicacoes.modelo.elastic;

import java.util.List;

/**
 * @author Helton
 *
 */
public class DataPublicacaoElastic {
	
	private List<BucketDataPublicacao> buckets;

	public List<BucketDataPublicacao> getBuckets() {
		return buckets;
	}

	public void setBuckets(List<BucketDataPublicacao> buckets) {
		this.buckets = buckets;
	}

	
	
}
