package com.cj.comparadores;

import java.util.Comparator;

import com.cj.pojos.GPSData;

public class GPSDataCompare implements Comparator<GPSData> {

	public int compare(GPSData arg0, GPSData arg1) {
		// TODO Auto-generated method stub
		return arg0.getIdRegistro().compareTo(arg1.getIdRegistro());
	}

}
