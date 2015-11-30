package ylj.house.mvc.controllers;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;

import java.io.InputStreamReader;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpSession;

import ylj.house.tmsf.data.property.Property;
import ylj.house.tmsf.data.property.PropertyAffairs;
import ylj.house.tmsf.data2.DaySaledHouse;
import ylj.house.tmsf.data2.DaySaledHouseAffairs;
import ylj.house.user.subscription.UserPropertySubscription;
import ylj.house.user.subscription.UserPropertySubscriptionAffairs;
import ylj.utils.ConnectionUtil;

@Controller("PropertyController")
public class PropertyController {

	static Logger logger=LoggerFactory.getLogger(PropertyController.class);

	
	public PropertyController() throws Exception {

		System.out.println("PropertyController created .");

	}

	@SuppressWarnings("restriction")
	
	public void init() {
		System.out.println("PropertyController init ...");

		// DOMConfigurator.configure(HouseController.class.getResource("/conf/log4j.xml"));

	}

	@SuppressWarnings("restriction")

	void destroy() {

	}

	
	//http://localhost:8080/housemvc/house_jstl?propertyId=26201346&dateFrom=2015-08-17&dateTo=2015-08-17
	//http://107.170.208.159:8080/housemvc/house_jstl?propertyId=26201346&dateFrom=2015-08-17&dateTo=2015-08-17
	@RequestMapping(value = "/property_jstl", method = RequestMethod.GET)
	public String handleHouse_JSTL(@RequestParam("propertyId") String propertyId, @RequestParam(value="dateFrom",required=false) String dateFrom,
			@RequestParam(value="dateTo",required=false) String dateTo, Model model,HttpSession session) throws Exception {

		// 0model.addAttribute("propertyId",propertyId);
		// model.addAttribute("date", date);
		if(dateTo==null||dateFrom==null){
			TimeZone zone = TimeZone.getTimeZone("GMT+8");  //时区

			SimpleDateFormat	ISO_time_format = new SimpleDateFormat("yyyy-MM-dd");
			ISO_time_format.setTimeZone(zone);	
			
			long dateToTime=System.currentTimeMillis();
			long dateFromTime=dateToTime-30L*24L*3600L*1000L;
			
			dateFrom=ISO_time_format.format(new Date(dateFromTime));	
			dateTo=ISO_time_format.format(new Date(dateToTime));	
		}
		
		
		String loginedAccount = (String) session.getAttribute("account");
		//session.setAttribute("account", loginedAccount);

		Property property=PropertyAffairs.getPropertyId(propertyId);
		
		UserPropertySubscription subscription=UserPropertySubscriptionAffairs.getUserSubscription(loginedAccount, propertyId);
		boolean isSubscripted=false;
		if(subscription==null){
			isSubscripted=false;
		}else{
			isSubscripted=true;
		}
		
		logger.info(loginedAccount+" query.     propertyId:" + propertyId+" dateFrom:" + dateFrom+"   dateTo:" + dateTo);
			
		List<DaySaledHouse> dayStates= 	DaySaledHouseAffairs.queryDaySaledHouse(propertyId, dateFrom,dateTo);	
		
		model.addAttribute("dailyStates", dayStates);
		model.addAttribute("property", property);
		model.addAttribute("dateFrom", dateFrom);
		model.addAttribute("dateTo", dateTo);
		model.addAttribute("isSubscripted", isSubscripted);
	
		// 
		// model.addAttribute("states", states);

		return "property_jstl";
	}
	
	/*
	
	@RequestMapping(value = "/house_json", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	@ResponseBody
	public String handleHouse_JSON(@RequestParam("propertyId") String propertyId, @RequestParam("dateFrom") String dateFrom,
			@RequestParam("dateTo") String dateTo) {

		// 0model.addAttribute("propertyId",propertyId);
		// model.addAttribute("date", date);

		System.out.println("     propertyId:" + propertyId);
		System.out.println("       dateFrom:" + dateFrom);
		System.out.println("         dateTo:" + dateTo);

		
		
		List<DaySaledHouse> dayStates= DaySaledHouseDBUtil.queryDaySaledHouse(propertyId, dateFrom,dateTo);
		
			
		


		return JSON.toJSONString(dayStates);
	}
	*/
}
