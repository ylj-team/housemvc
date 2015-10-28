package ylj.house.mvc.controllers;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;



import ylj.house.tmsf.data.property.Property;
import ylj.housesearch.sql.SQLSearcherOfProperty;


@Controller("PropertySearchController")
public class PropertySearchController {
	
	static Logger logger=LoggerFactory.getLogger(PropertySearchController.class);

	
	
	SQLSearcherOfProperty aSQLSearcherOfProperty;
	
	
	public static class SearchRecord {
		
		public String propertyTypeCode;
		public String propertyId;			
		public String propertyName;		
		public String city;
		public String district;	
		public String addr;	
		public String developer;	
		public String jumpUrl;
		public String getPropertyTypeCode() {
			return propertyTypeCode;
		}
		public void setPropertyTypeCode(String propertyTypeCode) {
			this.propertyTypeCode = propertyTypeCode;
		}
		public String getPropertyId() {
			return propertyId;
		}
		public void setPropertyId(String propertyId) {
			this.propertyId = propertyId;
		}
		public String getPropertyName() {
			return propertyName;
		}
		public void setPropertyName(String propertyName) {
			this.propertyName = propertyName;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getDistrict() {
			return district;
		}
		public void setDistrict(String district) {
			this.district = district;
		}
		public String getAddr() {
			return addr;
		}
		public void setAddr(String addr) {
			this.addr = addr;
		}
		public String getDeveloper() {
			return developer;
		}
		public void setDeveloper(String developer) {
			this.developer = developer;
		}
		public String getJumpUrl() {
			return jumpUrl;
		}
		public void setJumpUrl(String jumpUrl) {
			this.jumpUrl = jumpUrl;
		}
		
		
	}
	
	public PropertySearchController() throws Exception {

		System.out.println("PropertySearchController created .");
		aSQLSearcherOfProperty=new SQLSearcherOfProperty();
	}

	public static SearchRecord[] toSearchRecord(Property[] properties){
		
	
		 
		if(properties==null)
			return null;
		
		SimpleDateFormat	ISO_time_format = new SimpleDateFormat("yyyy-MM-dd");
		long sevenDayTime=7*24*3600*1000;
		
		 String dateFrom=ISO_time_format.format(new Date(System.currentTimeMillis()-sevenDayTime));	
		 String dateTo = ISO_time_format.format(new Date(System.currentTimeMillis()));	
		 
		 SearchRecord[] searchRecords=new SearchRecord[properties.length];
		 
		 for(int i=0;i<properties.length;i++){
			 
			 searchRecords[i]=new SearchRecord();
			 searchRecords[i].propertyTypeCode=properties[i].getPropertyType();
			 searchRecords[i].propertyId=properties[i].getPropertyId();
			 searchRecords[i].propertyName =properties[i].getPropertyName();
			 
			 searchRecords[i].addr=properties[i].getPropertyAddress();
			 searchRecords[i].district=properties[i].getDistrict();
			 searchRecords[i].city=properties[i].getCity();
			 searchRecords[i].developer =properties[i].getDeveloper();
			 
			 String url="./property_jstl?propertyId="+ searchRecords[i].propertyId+"&dateFrom="+dateFrom+"&dateTo="+dateTo;
				
			 searchRecords[i].jumpUrl=url;
			 
		 }
		 
		 return searchRecords;
	}
	
	@RequestMapping(value = "/do_PropertySearch_jstl", method = RequestMethod.GET)
	public String handlePropertySearch_JSTL(@RequestParam(value="query", required=false) String query, Model model,HttpSession session) throws Exception {

		
		 String dateFrom;
		 String dateTo;
		// 0model.addAttribute("propertyId",propertyId);
		// model.addAttribute("date", date);		
		
	//	String query="涓囩";
		//long total=aSQLSearcherOfProperty.searchByPropertyNameTotalHit(query);
		 
		 	byte bb[]; 
		 	if(query!=null){
		 		
		 		bb = query.getBytes("ISO-8859-1"); //浠�"ISO-8859-1"鏂瑰紡瑙ｆ瀽name瀛楃涓� 
		 		query= new String(bb, "UTF-8"); //鍐嶇敤"utf-8"鏍煎紡琛ㄧずname     
	        
		 		Property[] properties=aSQLSearcherOfProperty.searchByPropertyName(query);
		 		
		 		logger.info("query:"+query+" result=>"+properties.length);
		 		
		 		
		 		SearchRecord[] searchResults=toSearchRecord(properties);
		 		
		 		model.addAttribute("searchResultSize", searchResults.length);
		 		model.addAttribute("searchResults", searchResults);
		 		model.addAttribute("query", query);
		
		 		
		 }else{
			 logger.info("query == null.");
			 model.addAttribute("searchResultSize", 0);
		 }
		 	
		 	
		return "property_search_jstl";

	}
}
