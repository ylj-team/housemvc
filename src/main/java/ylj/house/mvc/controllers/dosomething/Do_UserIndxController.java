package ylj.house.mvc.controllers.dosomething;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller("Do_UserIndxController")
public class Do_UserIndxController {

	static Logger logger = LoggerFactory.getLogger(Do_UserIndxController.class);

	private void setRedirectToLogin(HttpServletResponse httpResponse, String nextUrl,String message) throws IOException {
		//
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		// errorMessage=errorMessage.replaceAll(" ", "%20");
		params.add(new BasicNameValuePair("message", message));
		params.add(new BasicNameValuePair("nextUrl", nextUrl));

		String query = URLEncodedUtils.format(params, "UTF-8");
		query = query.replace("+", "%20");
		// URLEncodedUtils.f
		// String
		// query=URLEncodedUtils.format(params,Charset.defaultCharset());
		// logger.info("query: " + query);
		// logger.info("query: " + URLEncoder.encode(errorMessage));

		List<NameValuePair> params2 = URLEncodedUtils.parse(query, Charset.forName("UTF-8"));
		// List<NameValuePair> params2 =URLEncodedUtils.parse(query,
		// Charset.defaultCharset());
		// logger.info("params2 " + params2);

		String reloginUrl = "./login.html?" + query;
		logger.info("Redirect to " + reloginUrl);

		httpResponse.sendRedirect(reloginUrl);
	}

	
	@RequestMapping(value = "/userIdx", method = { RequestMethod.POST, RequestMethod.GET })
	public String handleUser(HttpSession session, Model model,HttpServletResponse httpResponse) throws Exception {
		

			Boolean login = (Boolean) session.getAttribute("login");
			String loginedAccount = (String) session.getAttribute("account");

			// already logined
			if (login != null && login == true) {
				model.addAttribute("account", loginedAccount);		
				return "user_index_jstl";
			}else{
				setRedirectToLogin( httpResponse,"/userIdx","Î´µÇÂ½");
				return null;
			}
					
	}
}
