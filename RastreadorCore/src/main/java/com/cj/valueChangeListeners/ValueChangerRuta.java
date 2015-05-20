package com.cj.valueChangeListeners;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;

import com.googlecode.gmaps4jsf.component.common.Position;
import com.googlecode.gmaps4jsf.component.marker.Marker;

public class ValueChangerRuta implements ValueChangeListener{

	public void processValueChange(ValueChangeEvent arg0)
			throws AbortProcessingException {
		// TODO Auto-generated method stub
		Marker n=(Marker)arg0.getComponent();
		Position position=(Position)arg0.getNewValue();
		n.setLatitude(position.getLatitude());
		n.setLongitude(position.getLongitude());
	}

}
