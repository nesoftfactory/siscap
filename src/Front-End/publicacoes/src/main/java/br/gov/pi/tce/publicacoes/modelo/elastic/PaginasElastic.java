package br.gov.pi.tce.publicacoes.modelo.elastic;

import java.util.List;

public class PaginasElastic {
	
	public List<BucketPagina> buckets;

	public List<BucketPagina> getBuckets() {
		return buckets;
	}

	public void setBuckets(List<BucketPagina> buckets) {
		this.buckets = buckets;
	}


	
	

}
