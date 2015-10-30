package ylj.house.mvc.controllers.dosomething;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ylj.house.mvc.controllers.DailySaleController;
import ylj.house.user.UserAffairs;

@Controller("Do_SubscribeController")
public class Do_SubscribeController {
	
	static Logger logger=LoggerFactory.getLogger(Do_SubscribeController.class);


	public Do_SubscribeController() throws Exception{
		
		System.out.println("Do_SubscribeController created .");
		
	}
	
	
	@RequestMapping(value = "/do_subscribe", method = RequestMethod.GET)
	public void handleSubscribe(@RequestParam("propertyId") String propertyId,Model model,HttpSession session,HttpServletResponse httpResponse) throws IOException {

			
		//设置cookie
		//HttpServletResponse response.addCookie(new Cookie("foo", "bar"));
		String loginedAccount = (String) session.getAttribute("account");
		
		//设置session	
	
		
		String propertyUrl = "./property_jstl" ;
		logger.info("Redirect to " + propertyUrl);

		httpResponse.sendRedirect(propertyUrl);
		
		return ;
	}
}
