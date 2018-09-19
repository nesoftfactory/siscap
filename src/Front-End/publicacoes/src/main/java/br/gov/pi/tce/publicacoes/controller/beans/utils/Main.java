package br.gov.pi.tce.publicacoes.controller.beans.utils;

import java.util.Date;

public class Main {
	
	// URL das fontes dos diários oficiais
	public final static String URL_FONTE_DIARIO_OFICIAL_PARNAIBA = "http://dom.parnaiba.pi.gov.br";
	public final static String URL_FONTE_DIARIO_OFICIAL_TERESINA = "http://www.dom.teresina.pi.gov.br";
	public final static String URL_FONTE_DIARIO_OFICIAL_DOS_MUNICIPIOS = "http://www.diarioficialdosmunicipios.org";
	public final static String URL_FONTE_DIARIO_OFICIAL_PIAUI = "http://www.diariooficial.pi.gov.br";

    public static void main(String[] args) {

    	ColetorPublicacaoUtil coletorPublicacaoUtil = new ColetorPublicacaoUtil();
    	//Parnaiba
//        Date dataInicial =  coletorPublicacaoUtil.getData("01/01/2017 00:00:00");
//        Date dataFinal = coletorPublicacaoUtil.getData("19/09/2018 23:59:59");        
    	//Teresina
//        Date dataInicial =  coletorPublicacaoUtil.getData("07/01/2005 00:00:00");
//        Date dataFinal = coletorPublicacaoUtil.getData("19/09/2018 23:59:59");
        //Piaui
//        Date dataInicial =  coletorPublicacaoUtil.getData("01/08/2018 00:00:00");
//        Date dataFinal = coletorPublicacaoUtil.getData("19/09/2018 23:59:59");
    	//Municipios
        Date dataInicial =  coletorPublicacaoUtil.getData("01/07/2018 00:00:00");
        Date dataFinal = coletorPublicacaoUtil.getData("19/09/2018 23:59:59");

        // Vetor de url das fontes
        String[] fontes = new String[]{
//                URL_FONTE_DIARIO_OFICIAL_PIAUI//,
                URL_FONTE_DIARIO_OFICIAL_DOS_MUNICIPIOS//,
//        		URL_FONTE_DIARIO_OFICIAL_TERESINA//,
//        		URL_FONTE_DIARIO_OFICIAL_PARNAIBA
        };

        for (String fonte : fontes) {
            switch (fonte) {
                case URL_FONTE_DIARIO_OFICIAL_PIAUI:
                	coletorPublicacaoUtil.getDiariosEmDiarioOficialPI(dataInicial, dataFinal);
                    break;
                case URL_FONTE_DIARIO_OFICIAL_DOS_MUNICIPIOS:
                	coletorPublicacaoUtil.getDiariosDOM(URL_FONTE_DIARIO_OFICIAL_DOS_MUNICIPIOS, dataInicial, dataFinal);
                    break;
                case URL_FONTE_DIARIO_OFICIAL_TERESINA:
                	coletorPublicacaoUtil.getDiariosDOM(URL_FONTE_DIARIO_OFICIAL_TERESINA, dataInicial, dataFinal);
                    break;
                case URL_FONTE_DIARIO_OFICIAL_PARNAIBA:
                	coletorPublicacaoUtil.getDiariosDOM(URL_FONTE_DIARIO_OFICIAL_PARNAIBA, dataInicial, dataFinal);
                    break;
            }
        }
        
//        Date a = new Date();
//        System.out.println(a);
//        a.setDate(a.getDate() + 10);  
//        System.out.println(a);
        
    }
}