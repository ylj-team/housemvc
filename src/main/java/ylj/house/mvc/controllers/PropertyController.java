package ylj.house.mvc.controllers;

import java.util.List;

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














import javax.servlet.http.HttpSession;

import ylj.house.tmsf.data.property.Property;
import ylj.house.tmsf.data.property.PropertyAffairs;
import ylj.house.tmsf.data2.DaySaledHouse;
import ylj.house.tmsf.data2.DaySaledHouseAffairs;
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


	// http://localhost:8080/housemvc/house?propertyId=64897079&dateFrom=2015-07-18&dateTo=2015-07-20

	
	
	//http://localhost:8080/housemvc/house_jstl?propertyId=26201346&dateFrom=2015-08-17&dateTo=2015-08-17
	//http://107.170.208.159:8080/housemvc/house_jstl?propertyId=26201346&dateFrom=2015-08-17&dateTo=2015-08-17
	@RequestMapping(value = "/property_jstl", method = RequestMethod.GET)
	public String handleHouse_JSTL(@RequestParam("propertyId") String propertyId, @RequestParam("dateFrom") String dateFrom,
			@RequestParam("dateTo") String dateTo, Model model,HttpSession session) throws Exception {

		// 0model.addAttribute("propertyId",propertyId);
		// model.addAttribute("date", date);
		
		String loginedAccount = (String) session.getAttribute("account");
		//session.setAttribute("account", loginedAccount);

		Property  property=PropertyAffairs.getPropertyId(propertyId);
		
		logger.info(loginedAccount+" query.     propertyId:" + propertyId+" dateFrom:" + dateFrom+"   dateTo:" + dateTo);
			
		List<DaySaledHouse> dayStates= 	DaySaledHouseAffairs.queryDaySaledHouse(propertyId, dateFrom,dateTo);	
		
		model.addAttribute("dailyStates", dayStates);
		model.addAttribute("property", property);
//		model.addAttribute("propertyId", propertyId);
		model.addAttribute("dateFrom", dateFrom);
		model.addAttribute("dateTo", dateTo);
		// model.addAttribute("dailySigneds", dailySigneds);
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
