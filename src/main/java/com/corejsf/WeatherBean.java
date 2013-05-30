package com.corejsf;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.xml.ws.WebServiceRef;

import com.cdyne.ws.weatherws.Forecast;
import com.cdyne.ws.weatherws.ForecastReturn;
import com.cdyne.ws.weatherws.Weather;

@Named
@SessionScoped
public class WeatherBean implements Serializable {

	private static final long serialVersionUID = 1L;

	// Doesn't work!
//	@WebServiceRef(wsdlLocation="http://ws.cdyne.com/WeatherWS/Weather.asmx?wsdl")
//	private Weather service;

	private String zip;
	private String city;
	private List<Forecast> response;
	
	public String getZip() {
		return zip;
	}
	
	public void setZip(String zip) {
		this.zip = zip;
	}
	
	public String getCity() {
		return city;
	}
	
	public List<Forecast> getResponse() {
		return response;
	}
	
	public String search() {
		try {
			Weather service = new Weather();
			ForecastReturn ret = service.getWeatherSoap().getCityForecastByZIP(zip);
			response = ret.getForecastResult().getForecast();

			for(Forecast f : response) {
				if (f.getDesciption() == null || f.getDesciption().length() == 0) {
					f.setDesciption("Not Available");
				}
			}
			city = ret.getCity();
			
			return "success";
			
		} catch (Exception e) {
			Logger.getLogger("com.corejsf").log(Level.SEVERE, "Remote call failed", e);
			
			return "error";
		}
	}
	
}
