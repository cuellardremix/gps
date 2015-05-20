package com.cj.ReportesDAO;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;

import com.cj.pojos.Perfil;

public class ReportesPerfilesDAO {
	private JasperPrint jasperPrint;
	
	public void crearReportePerfilesGeneral(List<Perfil> perfiles) {
try {
    		

    		Map<String,Object> parameters = new HashMap<String,Object>();

    		
    		ClassLoader classLoader = getClass().getClassLoader();
    		InputStream url = null;
    		url = classLoader.getResourceAsStream("test.jasper");

			jasperPrint= JasperFillManager.fillReport( classLoader.getResource("Perfiles.jasper").getPath(), parameters, new JRBeanCollectionDataSource(perfiles));
 
			
			HttpServletResponse httpServletResponse=(HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
			httpServletResponse.addHeader("Content-disposition", "attachment; filename=report.xls");
			ServletOutputStream servletOutputStream;
			try {
				servletOutputStream = httpServletResponse.getOutputStream();
			       JRXlsxExporter docxExporter=new JRXlsxExporter();  
			       docxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);  
			       docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);  
			       docxExporter.exportReport();  
				FacesContext.getCurrentInstance().responseComplete();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			  
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
