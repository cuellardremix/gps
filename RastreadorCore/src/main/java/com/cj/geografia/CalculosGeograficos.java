package com.cj.geografia;

import java.util.ArrayList;
import java.util.List;

import com.cj.pojos.Cerca;
import com.cj.pojos.GPSData;
import com.cj.pojos.Ruta;

public class CalculosGeograficos {

	public static final Double RadioTierra=6372.795477598;
	
	
	   public static final double R = 6372.8; // In kilometers
	   public static final double grado=111.319;//en kilometros
	   
	   public static double haversine(GPSData a,GPSData b) { 
	   double lat1=a.getLatitude();
	   double lon1=a.getLongitude();
	   double lat2=b.getLatitude();
	   double lon2=b.getLongitude();
	        double dLat = Math.toRadians(lat2 - lat1);
	        double dLon = Math.toRadians(lon2 - lon1);
	        lat1 = Math.toRadians(lat1);
	        lat2 = Math.toRadians(lat2);
	 
	        double a1 = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
	        double c = 2 * Math.asin(Math.sqrt(a1));
	        return R * c;
	    }
	
	public static Double distanciaAB(GPSData a, GPSData b){
		Double distancia=RadioTierra*Math.acos(Math.sin(a.getLatitude())*Math.sin(b.getLatitude()))
				+Math.cos(a.getLatitude())*Math.cos(b.getLatitude())*Math.cos(a.getLongitude()-b.getLongitude());
		return distancia;
	}
	
	public static Double kmAGrado(Double kms){
		Double km=1/CalculosGeograficos.grado;
		return km*kms;
	}

	public static Double mphAkmph(Double mph){
		return mph*(1.609);
	}
	
	public static Boolean isInCerca(List<GPSData> puntos,List<GPSData> marcas){
		Boolean in=true;
		for(GPSData m:marcas){
			if(!isInPolygon(puntos, m)){
				return false;
			}
		}
		return in;
	}
	
	public static Boolean isInPolygon(List<GPSData> puntos,GPSData punto){
		int numPoints=puntos.size();
		boolean inPoly=false;
		int j=numPoints-1;
		List<GPSData> puntosAlReves=new ArrayList<GPSData>();
		for(int i=puntos.size(); i==0; i--){
			puntosAlReves.add(puntos.get(i));
		}
		puntos.addAll(puntosAlReves);
		for(int i=0; i<numPoints; i++){
			GPSData vertex1=puntos.get(i);
			GPSData vertex2=puntos.get(j);
			if(vertex1.getLongitude() <punto.getLongitude() && vertex2.getLongitude()>=
					punto.getLongitude() || vertex2.getLongitude()<punto.getLongitude() && vertex1.getLongitude()
					>=punto.getLongitude()){
				if(vertex1.getLatitude()+(punto.getLongitude() - vertex1.getLongitude())/(vertex2.getLongitude()-vertex1.getLongitude())
						*(vertex2.getLatitude()-vertex1.getLatitude())<punto.getLatitude()){
					inPoly=!inPoly;
				}
			}
			j=i;
		}
		return inPoly;
	}
	
	public static Boolean isInRuta(List<GPSData> datos, List<GPSData> puntos, Ruta ruta){
		Boolean adentro=false;
		Integer posi=0;
		for(GPSData dato:datos){
			for(int i=posi; i<puntos.size(); i++){
				if(CalculosGeograficos.haversine(puntos.get(i), dato)<=ruta.getRutAnc()){
					if(posi==0)
						posi=i;
					adentro=true;
				}
			}
			
		}
		return adentro;
	}
	
//	public static Boolean isInCerca(List<GPSData> datos, List<GPSData> puntos, Cerca cerca){
//		Boolean adentro=false;
//		Integer posi=0;
//		for(GPSData dato:datos){
//			for(int i=posi; i<puntos.size(); i++){
//				if(CalculosGeograficos.haversine(puntos.get(i), dato)<=cerca.getRadio()){
//					adentro=true;
//					if(posi==0)
//					posi=i;
//				}	
//			}
//			
//		}
//		return adentro;
//	}
}
