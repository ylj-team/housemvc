package ylj.house.mvc.controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;






import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import java.io.InputStreamReader;







import ylj.house.tmsf.data.salestate.PropertyDailySigned;
import ylj.house.tmsf.data.salestate.PropertyDailySignedAffairs;
import ylj.utils.ConnectionUtil;


@Controller("DailySaleControl")

public class DailySaleController {

	static Logger logger=LoggerFactory.getLogger(DailySaleController.class);

	TimeZone zone =null;
	SimpleDateFormat ISO_time_format=null;
	
	List<String> sidsOfHZ;
	List<String> sidsOfXS;
	List<String> sidsOfYH;
	
	public DailySaleController() throws Exception{
		
		System.out.println("DailySaleController created .");
		
		zone = TimeZone.getTimeZone("GMT+8");  //鏃跺尯

		ISO_time_format = new SimpleDateFormat("yyyy-MM-dd");
		ISO_time_format.setTimeZone(zone);
	
		
		
		 sidsOfHZ=new LinkedList<String>();		
			sidsOfHZ.add("33");
			sidsOfHZ.add("330102");
			sidsOfHZ.add("330103");
			sidsOfHZ.add("330104");
			sidsOfHZ.add("330105");		
			sidsOfHZ.add("330103");
			sidsOfHZ.add("330108");
			sidsOfHZ.add("330110");
			sidsOfHZ.add("330186");
			sidsOfHZ.add("330231");
	
			sidsOfXS=new LinkedList<String>();			
			sidsOfXS.add("330181");			
			
			
			sidsOfYH=new LinkedList<String>();		
			sidsOfYH.add("330184");
	}
	
	@SuppressWarnings("restriction")
	@PostConstruct
	public void init() {
		System.out.println("DailySaleController init ...");

	//	DOMConfigurator.configure(HouseController.class.getResource("/conf/log4j.xml"));
		
	}
	
	@SuppressWarnings("restriction")
	@PreDestroy
	void destroy() {

	}


	
	//http://localhost:8080/housemvc/daily?date=2015-07-10
	    public static class SignedRecord{
	    	    		    	
	    	PropertyDailySigned houseDailySigned;
	    	
	    	
	    	public SignedRecord(PropertyDailySigned houseDailySigned){
	    		this.houseDailySigned=houseDailySigned;
	    	}
			
			public String getSignedNumber(){
				return houseDailySigned.signedNumber;
			}
			public String getReservedNumber(){
				return houseDailySigned.reservedNumber;
			}
			public String getSignedArea(){
				return houseDailySigned.signedArea;
			}
			public String getSignedAvgPrice(){
				return houseDailySigned.signedAvgPrice;
			}
			public String getSignedTime(){
				return houseDailySigned.signedTime;
			}
			
	    }
		public static class PropertySignedRecord {
			
			public String propertyTypeCode;
			public String propertyId;			
			public String propertyName;		
			public String district;		
			public String signedDate;
			
			public String url;
			
			public List<SignedRecord> signedRecords;

			
			public String getPropertyTypeCode(){
				return propertyTypeCode;
			}
			public String getPropertyId(){
				return propertyId;
			}
			public String getPropertyName(){
				return propertyName;
			}
			public String getDistrict(){
				return district;
			}
			public String getSignedDate(){
				return signedDate;
			}
			public String getUrl(){
				return url;
			}
			public List<SignedRecord> getSignedRecords(){
				return signedRecords;
			}
			
		}
	
		
		
		
		//http://localhost:8080/housemvc/daily_jstl?date=2015-08-17
		@RequestMapping(value = "/daily_jstl", method = RequestMethod.GET)
		public String handleDaily_JSTL(@RequestParam(value="date",required=false) String date,@RequestParam(value="city",required=false) String city,Model model, HttpSession session) throws Exception {
			//0model.addAttribute("propertyId",propertyId);
		    //model.addAttribute("date", date);
		
			//System.out.println("      date:"+date);	
			if(date==null){
				date=ISO_time_format.format(new Date(System.currentTimeMillis()));		
			}	
					
			String loginedAccount = (String) session.getAttribute("account");
			session.setAttribute("account", loginedAccount);
						
		//	StringBuffer aStringBuffer=new StringBuffer();
		
			String cityName="全部";
			
			List<PropertyDailySigned> dailySigneds=null;
			if(null==city){
				dailySigneds=PropertyDailySignedAffairs.getRecordsOfDay(date);
				cityName="全部";
			}else if ("hz".equals(city.toLowerCase())){
				dailySigneds=PropertyDailySignedAffairs.getRecordsOfDaySids(date, sidsOfHZ);
				cityName="杭州主城区";
			}else if ("xs".equals(city.toLowerCase())){
				dailySigneds=PropertyDailySignedAffairs.getRecordsOfDaySids(date, sidsOfXS);
				cityName="萧山区";
			}else if ("yh".equals(city.toLowerCase())){
				dailySigneds=PropertyDailySignedAffairs.getRecordsOfDaySids(date, sidsOfYH);
				cityName="余杭区";
			}else{
				dailySigneds=PropertyDailySignedAffairs.getRecordsOfDay(date);
				cityName="全部";
			}
			
		//	System.out.println(dailySigneds.size());
		//	aStringBuffer.append(dailySigneds.size()+"\n");
			logger.info("total dailySigned record: "+dailySigneds.size());
			
			
			Map<String,List<PropertyDailySigned>> propertySaleRecords=new TreeMap<String,List<PropertyDailySigned>>();
			for(PropertyDailySigned dailySigned:dailySigneds){
	
				List<PropertyDailySigned> saleRecords=propertySaleRecords.get(dailySigned.propertyName);
				if(saleRecords==null){
					saleRecords=new LinkedList<PropertyDailySigned>();
					propertySaleRecords.put(dailySigned.propertyName, saleRecords);
				}
				
				saleRecords.add(dailySigned);
			}
			
			//http://localhost:8080/housemvc/house?propertyId=64897079&date=2015-07-13
			String dateTo=date;
			long dateTime=ISO_time_format.parse(date).getTime();
			long dateFromTime=dateTime-30L*24L*3600L*1000L;
			String dateFrom=ISO_time_format.format(new Date(dateFromTime));
			
			logger.info("default time range, dateFrom:"+dateFrom+" dateTo:"+dateTo);
			
			List<PropertySignedRecord> propertyRecords=new LinkedList<PropertySignedRecord>();
		
			
			for(Entry<String,List<PropertyDailySigned>> entry:propertySaleRecords.entrySet()){
		//		aStringBuffer.append("--------------- "+entry.getKey()+" -------------\n");
				String propertyName=entry.getKey();
				String url=null;
				String propertyId=null;
				String propertyTypeCode=null;
				String district=null;
				String signedDate=null;
				
				List<SignedRecord> signedRecords=new LinkedList<SignedRecord>();			
				
				for(PropertyDailySigned dailySigned:entry.getValue()){
					
					 url="./property_jstl?propertyId="+dailySigned.propertyId+"&dateFrom="+dateFrom+"&dateTo="+dateTo;
					 propertyId=dailySigned.propertyId;
					 propertyTypeCode=dailySigned.propertyTypeCode;
					 district=dailySigned.district;
					 signedDate=dailySigned.signedDate;
					 
					 SignedRecord signedRecord=new SignedRecord(dailySigned);
					 signedRecords.add(signedRecord);
				}
				
			//	propertySignedRecord.district
				
				PropertySignedRecord newPropertySignedRecord=new PropertySignedRecord();
				newPropertySignedRecord.district=district;
				newPropertySignedRecord.propertyId=propertyId;
				newPropertySignedRecord.propertyName=propertyName;
				newPropertySignedRecord.propertyTypeCode=propertyTypeCode;
				newPropertySignedRecord.signedDate=signedDate;
				newPropertySignedRecord.url=url;
				newPropertySignedRecord.signedRecords=signedRecords;
				
				
				propertyRecords.add(newPropertySignedRecord);
					
			}
			
			
			model.addAttribute("propertyRecords", propertyRecords);
			model.addAttribute("cityName", cityName);
			model.addAttribute("date", date);

		   
			return "daily_jstl";
		}
		
		/*
		@RequestMapping(value = "/daily_json", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
		@ResponseBody
		public String handleDaily_JSON(@RequestParam("date") String date) {
			//0model.addAttribute("propertyId",propertyId);
		    //model.addAttribute("date", date);
		
			System.out.println("      date:"+date);
			
		//	StringBuffer aStringBuffer=new StringBuffer();
		
			List<HouseDailySigned> dailySigneds= HouseDailySignedDBUtil.queryRecords(date);
			System.out.println(dailySigneds.size());
		//	aStringBuffer.append(dailySigneds.size()+"\n");
			
			Map<String,List<HouseDailySigned>> propertySaleRecords=new TreeMap<String,List<HouseDailySigned>>();
			for(HouseDailySigned dailySigned:dailySigneds){
	
				List<HouseDailySigned> saleRecords=propertySaleRecords.get(dailySigned.propertyName);
				if(saleRecords==null){
					saleRecords=new LinkedList<HouseDailySigned>();
					propertySaleRecords.put(dailySigned.propertyName, saleRecords);
				}
				
				saleRecords.add(dailySigned);
			}
			
			//http://localhost:8080/housemvc/house?propertyId=64897079&date=2015-07-13
			
			List<PropertySignedRecord> propertyRecords=new LinkedList<PropertySignedRecord>();
			
			for(Entry<String,List<HouseDailySigned>> entry:propertySaleRecords.entrySet()){
		//		aStringBuffer.append("--------------- "+entry.getKey()+" -------------\n");
				String propertyName=entry.getKey();
				String url=null;
				String propertyId=null;
				String propertyTypeCode=null;
				String district=null;
				String signedDate=null;
				
				List<SignedRecord> signedRecords=new LinkedList<SignedRecord>();
				
				
				for(HouseDailySigned dailySigned:entry.getValue()){
					
					 url="/housemvc/house_jstl?propertyId="+dailySigned.propertyId+"&dateFrom="+date+"&dateTo="+date;
					 propertyId=dailySigned.propertyId;
					 propertyTypeCode=dailySigned.propertyTypeCode;
					 district=dailySigned.district;
					 signedDate=dailySigned.signedDate;
					 
					 SignedRecord signedRecord=new SignedRecord(dailySigned);
					 signedRecords.add(signedRecord);
				}
				
			//	propertySignedRecord.district
				
				PropertySignedRecord newPropertySignedRecord=new PropertySignedRecord();
				newPropertySignedRecord.district=district;
				newPropertySignedRecord.propertyId=propertyId;
				newPropertySignedRecord.propertyName=propertyName;
				newPropertySignedRecord.propertyTypeCode=propertyTypeCode;
				newPropertySignedRecord.signedDate=signedDate;
				newPropertySignedRecord.url=url;
				newPropertySignedRecord.signedRecords=signedRecords;
				
				
				propertyRecords.add(newPropertySignedRecord);
					
			}
			
		
		   
			return JSON.toJSONString(propertyRecords);
		}
		*/
}
