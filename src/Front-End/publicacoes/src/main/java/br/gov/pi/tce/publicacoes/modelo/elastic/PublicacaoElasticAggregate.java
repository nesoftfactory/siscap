/**
 * 
 */
package br.gov.pi.tce.publicacoes.modelo.elastic;

/**
 * @author Helton
 *
 */
public class PublicacaoElasticAggregate {
	
	private Aggregations aggregations;

	public Aggregations getAggregations() {
		return aggregations;
	}

	public void setAggregations(Aggregations aggregations) {
		this.aggregations = aggregations;
	}
	
}
