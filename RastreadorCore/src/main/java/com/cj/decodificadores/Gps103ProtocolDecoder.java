package com.cj.decodificadores;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cj.geografia.CalculosGeograficos;
import com.cj.pojos.GPSData;

public class Gps103ProtocolDecoder {

	private static final Pattern pattern = Pattern.compile("imei:" + "(\\d+)," + // IMEI
			"([^,]+)," + // Alarm
			"(\\d{2})/?(\\d{2})/?(\\d{2})\\s?" + // Local Date
			"(\\d{2}):?(\\d{2})(?:\\d{2})?," + // Local Time
			"[^,]*," + "[FL]," + // F - full / L - low
			"(\\d{2})(\\d{2})(\\d{2})\\.(\\d+)," + // Time UTC (HHMMSS.SSS)
			"([AV])," + // Validity
			"(\\d+)(\\d{2}\\.\\d+)," + // Latitude (DDMM.MMMM)
			"([NS])," + "(\\d+)(\\d{2}\\.\\d+)," + // Longitude (DDDMM.MMMM)
			"([EW])?," + "(\\d+\\.?\\d*)," + // Speed
			"(\\d+\\.?\\d*)?,?" + // Course
			"(\\d+\\.?\\d*)?,?" + // Altitude
			"([^,]+)?,?" + "([^,]+)?,?" + "([^,]+)?,?" + "([^,]+)?,?" + ".*");

	public List<GPSData> decodificar(String sentenceBruto) {
		String[] sentencesDividido=sentenceBruto.split(";");
		List<GPSData> listaGPS=new ArrayList<GPSData>();
		String[] sentences=new String[sentencesDividido.length];
		List<String> temp=new ArrayList<String>();
		for(int i=0; i<sentencesDividido.length; i++){
			if(sentencesDividido[i]!=null){
			Matcher parser = pattern.matcher(sentencesDividido[i]);
			if(parser.matches()){
				temp.add(sentencesDividido[i]);
			}
			}
		}
		
		for(int i=0; i<temp.size(); i++){
			sentences[i]=temp.get(i);
		}
		
		if(sentences!=null)
		for(String sentence:sentences){
		GPSData gps = new GPSData();
		// Parse message
		if(sentence==null){
			if(!listaGPS.isEmpty())
				return listaGPS;
			else
				return null;
		}
		Matcher parser = pattern.matcher(sentence);
		if (!parser.matches()) {
			if(!listaGPS.isEmpty())
				return listaGPS;
			return null;
		}

		Integer index = 1;

		// Get device by IMEI
		String imei = parser.group(index++);
		// set imei
		gps.setImei(imei);
		// set alarm
		gps.setAlarm(parser.group(index++));

		// set fecha
		// Date
		Calendar time = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		time.clear();
		time.set(Calendar.YEAR, 2000 + Integer.valueOf(parser.group(index++)));
		time.set(Calendar.MONTH, Integer.valueOf(parser.group(index++)) - 1);
		time.set(Calendar.DAY_OF_MONTH, Integer.valueOf(parser.group(index++)));

		int localHours = Integer.valueOf(parser.group(index++));
		int localMinutes = Integer.valueOf(parser.group(index++));

		int utcHours = Integer.valueOf(parser.group(index++));
		int utcMinutes = Integer.valueOf(parser.group(index++));

		// Time
		time.set(Calendar.HOUR_OF_DAY, localHours);
		time.set(Calendar.MINUTE, localMinutes);
		time.set(Calendar.SECOND, Integer.valueOf(parser.group(index++)));
		time.set(Calendar.MILLISECOND, Integer.valueOf(parser.group(index++)));

		// Timezone calculation
		int deltaMinutes = (localHours - utcHours) * 60 + localMinutes
				- utcMinutes;
		if (deltaMinutes <= -12 * 60) {
			deltaMinutes += 24 * 60;
		} else if (deltaMinutes > 12 * 60) {
			deltaMinutes -= 24 * 60;
		}
		time.add(Calendar.MINUTE, -deltaMinutes);
		gps.setFecha(time.getTime());

		// valido
		gps.setValid(parser.group(index++).compareTo("A") == 0);
		
		//latitude
	    Double latitude = Double.valueOf(parser.group(index++));
        latitude += Double.valueOf(parser.group(index++)) / 60;
        if (parser.group(index++).compareTo("S") == 0) latitude = -latitude;
        gps.setLatitude(latitude);
        
        
        //longitud
        Double longitude = Double.valueOf(parser.group(index++));
        longitude += Double.valueOf(parser.group(index++)) / 60;
        String hemisphere = parser.group(index++);
        if (hemisphere != null) {
            if (hemisphere.compareTo("W") == 0) longitude = -longitude;
        }
        gps.setLongitude(longitude);
        
        //velocidad
        gps.setSpeed(CalculosGeograficos.mphAkmph(Double.valueOf(parser.group(index++))));

        //curso
        String course = parser.group(index++);
        if (course != null) {
            gps.setCurso(Double.valueOf(course));
        } else {
            gps.setCurso(0.0);
        }
        //altitud
        String altitude = parser.group(index++);
        if (altitude != null) {
            gps.setAltitud(Double.valueOf(altitude));
        } else {
            gps.setAltitud(0.0);
        }
        
        // Datos adicionales
        String ioTemp=parser.group(index++);
        if(ioTemp!=null)
        if(!ioTemp.equals(";"))
        gps.setIo1(null);
        else
        	gps.setIo1(ioTemp);
        
        
        ioTemp=parser.group(index++);
//        if(!ioTemp.equals(";"))
        gps.setIo2(ioTemp);
        
        
        ioTemp=parser.group(index++);
//        if(!ioTemp.equals(";"))
        gps.setIo3(ioTemp);
        
        ioTemp=parser.group(index++);
//        if(!ioTemp.equals(";"))
        gps.setIo4(ioTemp);
        listaGPS.add(gps);
        
		}
        
		return listaGPS;
	}

}
