package br.gov.pi.tce.siscap.timer.util;

public class UriUtil {

	public static String encodeString(String palavra) {
		char one;
		StringBuffer n = new StringBuffer(palavra.length());
		for (int i = 0; i < palavra.length(); i++) {
			one = palavra.charAt(i);
			switch (one) {
			case ' ':
				n.append('%');
				n.append('2');
				n.append('0');
				break;
			default:
				n.append(one);
			}
		}
		return n.toString();
	}

}
