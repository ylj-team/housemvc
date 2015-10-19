package ylj.house.mvc.controllers;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ylj.house.tmsf.property.Property;
import ylj.housesearch.sql.SQLSearcherOfProperty;


@Controller("PropertySearchController")
public class PropertySearchController {
	
	static Logger logger=LoggerFactory.getLogger(PropertySearchController.class);

	
	
	SQLSearcherOfProperty aSQLSearcherOfProperty;
	
	
	
	public PropertySearchController() throws Exception {

		System.out.println("PropertySearchController created .");
		aSQLSearcherOfProperty=new SQLSearcherOfProperty();
	}

	@RequestMapping(value = "/do_PropertySearch_jstl", method = RequestMethod.GET)
	public String handlePropertySearch_JSTL(@RequestParam(value="query", required=false) String query, Model model,HttpSession session) throws Exception {

		
		 String dateFrom;
		 String dateTo;
		// 0model.addAttribute("propertyId",propertyId);
		// model.addAttribute("date", date);		
		
	//	String query="万科";
		//long total=aSQLSearcherOfProperty.searchByPropertyNameTotalHit(query);
		 
		 	byte bb[]; 
	        bb = query.getBytes("ISO-8859-1"); //以"ISO-8859-1"方式解析name字符串 
	        query= new String(bb, "UTF-8"); //再用"utf-8"格式表示name 
	        
			
		Property[] properties=aSQLSearcherOfProperty.searchByPropertyName(query);
		
		logger.info("query:"+query+" result=>"+properties.length);
		model.addAttribute("properties", properties);
		model.addAttribute("query", query);
		
		return "property_search_jstl";

	}
}
