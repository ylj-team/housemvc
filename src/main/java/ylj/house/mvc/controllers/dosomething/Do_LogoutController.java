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

@Controller("Do_LogoutController")
public class Do_LogoutController {
	
	static Logger logger=LoggerFactory.getLogger(Do_LogoutController.class);

	

	public Do_LogoutController() throws Exception{
		
		System.out.println("Do_UserLoginController created .");
		
	}
	@RequestMapping(value = "/do_logout", method = RequestMethod.GET)
	public void handleLogin(Model model,HttpSession session,HttpServletResponse httpResponse) throws IOException {

		
		
		//设置cookie
		//HttpServletResponse response.addCookie(new Cookie("foo", "bar"));
		
		//设置session	
	
		logger.info("set session invalidate");		
		session.invalidate();
		
		String reloginUrl = "./login.html" ;
		logger.info("Redirect to " + reloginUrl);

		httpResponse.sendRedirect(reloginUrl);
		
		return ;
	}
}
