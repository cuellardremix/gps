package com.cj.ReportesDAO;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;

import com.cj.dao.InterfazDAO;
import com.cj.pojos.Vehiculo;

public class ReportesVehiculosDAO {
    private JasperPrint jasperPrint;
   
    public void crearReporteVehiculosGeneral(List<Vehiculo> vehiculos){
    	try {
    		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

    		Map parameters = new HashMap();
//    		List<Vehiculo> vehiculos=interfazDAO.obtenerVehiculos();
    		
    		ClassLoader classLoader = getClass().getClassLoader();
    		InputStream url = null;
    		url = classLoader.getResourceAsStream("test.jasper");
    		System.out.println(url.toString());
    		System.out.println(classLoader.getResource("test.jasper").getPath());
    		System.out.println(new File(classLoader.getResource("test.jasper").getFile()).exists());
//			jasperReport = JasperCompileManager.compileReport("src/test.jrxml");
			jasperPrint= JasperFillManager.fillReport( classLoader.getResource("test.jasper").getPath(), parameters, new JRBeanCollectionDataSource(vehiculos));
//			JasperExportManager.exportReportToPdfFile(jasperPrint, "report.pdf"); 
			
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
