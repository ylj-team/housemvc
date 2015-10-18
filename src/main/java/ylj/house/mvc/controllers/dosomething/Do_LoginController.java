package ylj.house.mvc.controllers.dosomething;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.interfaces.RSAPrivateKey;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.util.encoders.UrlBase64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ylj.house.user.User;
import ylj.house.user.UserAffairs;

@Controller("Do_LoginController")
public class Do_LoginController {

	static Logger logger = LoggerFactory.getLogger(Do_LoginController.class);

	
	public Do_LoginController() throws Exception {

		System.out.println("Do_UserLoginController created .");

	}

	private void setRedirectToLogin(HttpServletResponse httpResponse, String nextUrl,String message) throws IOException {
		//
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// params.add(new BasicNameValuePair("message","账号密码错误"));
		
		// errorMessage=errorMessage.replaceAll(" ", "%20");
		params.add(new BasicNameValuePair("message", message));
		params.add(new BasicNameValuePair("nextUrl", nextUrl));

		// URLEncode.encode 会将空格转换为“+”，但是“+”又不在BBBBBBBBBBBBBBB说明的
		// 空格编码为"+" 而不是 %20
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

	private void setRedirectToLoginPasswdChange(HttpServletResponse httpResponse,String account, String message) throws IOException {
		//
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// params.add(new BasicNameValuePair("message","账号密码错误"));
	
		// errorMessage=errorMessage.replaceAll(" ", "%20");
		params.add(new BasicNameValuePair("account", account));
		params.add(new BasicNameValuePair("message", message));
		//params.add(new BasicNameValuePair("message", nextUrl));

		// URLEncode.encode 会将空格转换为“+”，但是“+”又不在BBBBBBBBBBBBBBB说明的
		// 空格编码为"+" 而不是 %20
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

		String reloginUrl = "./loginpasswordchange.html?" + query;
		logger.info("Redirect to " + reloginUrl);

		httpResponse.sendRedirect(reloginUrl);
	}
	
	

	@RequestMapping(value = "/do_login_passwd_change", method = { RequestMethod.POST, RequestMethod.GET })
	public String handleLoginPasswdChange(@RequestParam("account") String accountEncoded,
			@RequestParam("passwd") String passwdEncoded,
			@RequestParam("new_passwd_1") String new_passwd_1Encoded,
			HttpSession session, Model model,HttpServletResponse httpResponse) throws Exception {
		

		String accountText = null;
		String passwdText = null;
		String newPasswdText = null;

		try {
			if (accountEncoded != null && !"".equals(accountEncoded)) {
				accountText = new String(RSAUtils.decrypt(Hex.decode(accountEncoded.getBytes())));
			}
		} catch (Exception e) {
			logger.error("decode account failed");
		}
		try {
			if (passwdEncoded != null && !"".equals(passwdEncoded)) {
				passwdText = new String(RSAUtils.decrypt(Hex.decode(passwdEncoded.getBytes())));
			}
		} catch (Exception e) {
			logger.error("decode passwd failed");
		}
		try {
			if (new_passwd_1Encoded != null && !"".equals(new_passwd_1Encoded)) {
				newPasswdText = new String(RSAUtils.decrypt(Hex.decode(new_passwd_1Encoded.getBytes())));
			}
		} catch (Exception e) {
			logger.error("decode newPasswd failed");
		}

		boolean checkResult = UserAffairs.isPasswdRight(accountText, passwdText);
		logger.info("checkResult:" + checkResult);
		
		String message="update new Passwd succes";
		
		if(checkResult){
			
			try{
				UserAffairs.newPasswd(accountText, newPasswdText);
				message="update new Passwd success ";
				logger.info("update new Passwd success ");
			}catch(Exception e){
				message="update new Passwd failed";
				logger.error("update new Passwd failed ");
			}
			
		}else{
			logger.info("old Passwd not right ");
			message="old Passwd not right ";
		}
		
			//@RequestParam(required = false, value = "account") String accountEncoded, @RequestParam(required = false, value = "passwd") String passwdEncoded,
	
		setRedirectToLoginPasswdChange( httpResponse, accountText, message);
		
		return null;
					
	}
	
	@RequestMapping(value = "/user", method = { RequestMethod.POST, RequestMethod.GET })
	public String handleUser(HttpSession session, Model model,HttpServletResponse httpResponse) throws Exception {
		

			Boolean login = (Boolean) session.getAttribute("login");
			String loginedAccount = (String) session.getAttribute("account");

			// already logined
			if (login != null && login == true) {
				model.addAttribute("account", loginedAccount);		
				return "user_jstl";
			}else{
				setRedirectToLogin( httpResponse,"/user","请先登陆");
				return "user_jstl";
			}
					
	}
	
	@RequestMapping(value = "/do_login", method = { RequestMethod.POST, RequestMethod.GET })
	public String handleLogin(@RequestParam("account") String accountEncoded, @RequestParam( "passwd") String passwdEncoded,
			@RequestParam(value="nextUrl",required=false) String nextUrlEncoded,@RequestParam(value="captchaToken",required=false) String captchaToken
			,Model model, HttpSession session, HttpServletResponse httpResponse) throws Exception {

		Boolean login = (Boolean) session.getAttribute("login");
		String loginedAccount = (String) session.getAttribute("account");

		// already logined
		if (login != null && login == true) {
			model.addAttribute("account", loginedAccount);
			return "user_jstl";
		}

		//check captchaToken
	
		String captchaTokenInSession=(String)session.getAttribute("captchaToken");
		if(captchaTokenInSession!=null){
			if(!captchaTokenInSession.equals(captchaToken)){
				setRedirectToLogin(httpResponse, null,"验证码错误");
				logger.info("captchaToken check failed." );
				return null;
			}else{
				logger.info("captchaToken check success." );
			}
		}else{
			logger.info("captchaToken not set." );
		}
		
		
		// String accountCipher=accountEncoded;
		// String passwdCipher=passwdEncoded;

		String accountText = null;
		String passwdText = null;
		String nextUrl = null;

		try {
			if (nextUrlEncoded != null && !"".equals(nextUrlEncoded)) {
				nextUrl = new String(UrlBase64.decode(nextUrlEncoded.getBytes()));
			}
		} catch (Exception e) {
			logger.error("decode nextUrl failed nextUrlEncoded:"+nextUrlEncoded);
			setRedirectToLogin(httpResponse, null,"decode nextUrl failed");
			return null;
		}
		try {
			if (accountEncoded != null && !"".equals(accountEncoded)) {
				accountText = new String(RSAUtils.decrypt( Hex.decode(accountEncoded.getBytes())));
			}
		} catch (Exception e) {
			logger.error("decode account failed");
			setRedirectToLogin(httpResponse, nextUrl,"decode account failed");
			return null;
		}
		try {
			if (passwdEncoded != null && !"".equals(passwdEncoded)) {
				passwdText = new String(RSAUtils.decrypt( Hex.decode(passwdEncoded.getBytes())));
			}
		} catch (Exception e) {
			logger.error("decode passwd failed");
			setRedirectToLogin(httpResponse, nextUrl,"decode passwd failed");
			return null;
		}

		

		logger.info("account:"+accountEncoded + "=>" + accountText+".");
		logger.info(" passwd:"+passwdEncoded + "=>" + passwdText+".");
		logger.info("nextUrl:"+nextUrlEncoded + "=>" + nextUrl+".");
		
	
		User hitUser=UserAffairs.getUserPasswdRight(accountText, passwdText);
		
		if(hitUser==null){
			setRedirectToLogin(httpResponse, nextUrl,"账户密码错误 ");
			logger.info("login failed passwd errror,"+accountText+","+passwdText+"." );

			return null;
		}
		
		if(hitUser.getState()!=0){
			setRedirectToLogin(httpResponse, nextUrl,"账户状态不可以 state:"+hitUser.getState());
			logger.info("user state:"+hitUser.getState());

			return null;
		}

		logger.info("login success :" + hitUser.getAccount());

		
		// 设置session
		logger.info("set session, login=true");
		// mins
		session.setMaxInactiveInterval(7*24 * 60);
		
		session.setAttribute("login", true);
		session.setAttribute("account", accountText);

		if (nextUrl != null && !"".equals(nextUrl)) {
			httpResponse.sendRedirect(nextUrl);
			logger.info("Redirect to nextUrl:" + nextUrl);
			return null;
		} else {		
			model.addAttribute("account", accountText);		
			return "user_jstl";
		}

	

	}
}
