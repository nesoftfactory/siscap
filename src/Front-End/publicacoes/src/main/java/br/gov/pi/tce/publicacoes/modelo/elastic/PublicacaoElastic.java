/**
 * 
 */
package br.gov.pi.tce.publicacoes.modelo.elastic;

import java.util.List;

/**
 * @author Helton
 *
 */
public class PublicacaoElastic {
	
	private List<BucketPublicacao> buckets;

	public List<BucketPublicacao> getBuckets() {
		return buckets;
	}

	public void setBuckets(List<BucketPublicacao> buckets) {
		this.buckets = buckets;
	}

	
}
