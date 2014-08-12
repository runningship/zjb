package org.bc.web;

import net.sf.json.JSONObject;

public class ModelAndView {

	public String jsp;
	
	public String redirect;
	
	public String contentType;
	
	public JSONObject data = new JSONObject();
	
	public String returnText = "";
	public boolean encodeReturnText;
	public ModelAndView(){
		
	}
}
