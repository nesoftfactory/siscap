package br.gov.pi.tce.siscap.timer.util;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {

	public static List<LocalDate> getDiasUteis(Date dataInicial, Date dataFinal) {
		LocalDate lda = asLocalDate(dataInicial);
		LocalDate ldb = asLocalDate(dataFinal);

		List<LocalDate> dates = new ArrayList<>();
		for (LocalDate ld = lda; !ld.isAfter(ldb); ld = ld.plusDays(1)) {
			if (!ld.getDayOfWeek().equals(DayOfWeek.SATURDAY) && !ld.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
				dates.add(ld);
			}
		}
		return dates;
	}

	public static LocalDate asLocalDate(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static Date convertToDate(String date) {

		if (date.isEmpty()) {
			return null;
		}

		List<String> dateFormats = new ArrayList<String>(
				Arrays.asList("dd/MM/yyyy", "dd--MM-yyyy", "dd-MM-yyyy", "d' 'MMMM' de 'yyyy"));

		for (String dateFormatString : dateFormats) {
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString);
				dateFormat.setLenient(false);
				return dateFormat.parse(date.trim());
			} catch (ParseException pe) {
			}
		}
		return null;
	}

	public static String convertDateToString(Date data) {
		Format formatter = new SimpleDateFormat("dd/MM/yyyy");
		String dataStr = formatter.format(data);
		return dataStr;
	}

	public static Date convertStringToDate(String dataStr) {
		Date data = null;
		try {
			SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
			Date dataParseada = formatoData.parse(dataStr);
			data = dataParseada;
		} catch (ParseException e) {
			return null;
		}
		return data;
	}
	
	public static Date convertToDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}
	
	public static String convertLocalDateToString(LocalDate date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedString = date.format(formatter);
		return formattedString;
	}

	public static Date getData00Horas00Minutos00SeguntosMenosQuatidadeDias(Date data, int quatidadeDias){
		if (quatidadeDias <= 0) {
			quatidadeDias = 0;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		cal.add(Calendar.DAY_OF_MONTH, (quatidadeDias * -1));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	public static Date getData23Horas59Minutos59Seguntos(Date data){
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
	


}
