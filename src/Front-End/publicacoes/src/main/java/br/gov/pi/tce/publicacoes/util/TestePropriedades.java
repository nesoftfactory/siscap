package br.gov.pi.tce.publicacoes.util;

public class TestePropriedades {

	public static void main(String[] args) {
		Propriedades propriedades = Propriedades.getInstance();
		
		System.out.println(propriedades.getValor("HORA_COLTA_DIARIO_OFICIAL_DOS_MUNICIPIOS"));
		System.out.println(propriedades.getValor("URI_API"));
		System.out.println(propriedades.getValor("URI_USUARIOS"));
	}
}
