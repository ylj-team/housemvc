package ylj.house.mvc.controllers.dosomething;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.cage.Cage;
import com.github.cage.GCage;



@Controller("Do_Captcha")
public class Do_Captcha {
	
	static Logger logger = LoggerFactory.getLogger(Do_Captcha.class);

	
	
	 private static final Cage cage = new GCage();
	 
	 /**
	   * Generates a captcha token and stores it in the session.
	   */
//	  public static void generateToken(HttpSession session) {
	//    String token = cage.getTokenGenerator().next();
//
	//    session.setAttribute("captchaToken", token);
	   // markTokenUsed(session, false);
	//  }
	
	  /**
	   * Marks token as used/unused for image generation.
	   */
	//  protected static void markTokenUsed(HttpSession session, boolean used) {
	//    session.setAttribute("captchaTokenUsed", used);
	//  }
	  /**
	   * Checks if the token was used/unused for image generation.
	 * @throws IOException 
	   */
	//  protected static boolean isTokenUsed(HttpSession session) {
	//    return !Boolean.FALSE.equals(session.getAttribute("captchaTokenUsed"));
	//  }
	  
	 //,"/captcha.jpg"
	 
	// String 
		static final int ApplyCodeLength = 6;
		public static String generateCaptchaToken() {

		
			String code = "";
			for (int i = 0; i < ApplyCodeLength; i++) {
				code = code + ((int) (Math.random() * 10));
			}

			return code;
		}
	 
	@RequestMapping(value = {"/do_captcha"}, method = { RequestMethod.POST, RequestMethod.GET })
	public void handleDoCaptcha(HttpSession session,HttpServletResponse httpRespons) throws IOException{
	
			//String token = cage.getTokenGenerator().next();
			String token =	generateCaptchaToken();
			
		    session.setAttribute("captchaToken", token); 
		    setResponseHeaders(httpRespons);

			logger.info("set session.captchaToken:"+token);
			
			logger.info("drawing... captchaToken");
			
		    cage.draw(token, httpRespons.getOutputStream());
		    
			logger.info("draw and send captcha graph complete.");
			
	}
	
	
	 protected void setResponseHeaders(HttpServletResponse resp) {
		    resp.setContentType("image/" + cage.getFormat());
		    resp.setHeader("Cache-Control", "no-cache, no-store");
		    resp.setHeader("Pragma", "no-cache");
		    long time = System.currentTimeMillis();
		    resp.setDateHeader("Last-Modified", time);
		    resp.setDateHeader("Date", time);
		    resp.setDateHeader("Expires", time);
		  }
}
