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

import ylj.house.user.UserAffairs;

@Controller("Do_LoginController")
public class Do_LoginController {

	static Logger logger = LoggerFactory.getLogger(Do_LoginController.class);

	RSAPrivateKey priKey = null;

	static String Modulus_hex = "00" + "bbea718283a23953a59cf2f14c177a7d1bc82c6109b6e97f7a495c3869b1db1e" + "546971de9a3a13d711b444deee67bb6c9f215d7e89ae3e1366b45ddef8be3c9a"
			+ "02920324e98d0f9dffc8414ce6e6e12b986c62fa60e5b4149ab0f693c2cc6816" + "5dc861030adcdede7589d5ba1fa1cedf8bdf4d1565db7ee264ae37633621aeef";

	static String Public_exponent_hex = "010001";

	static String Private_exponent_hex = "" + "02c2243c68363f652cef2ad9c3e62c541dce48687c3e051b6bee1bbe703ebe1a"
			+ "a9de8a5f5c20321e5c122b58a2633f6b0ec2ec9e68e2f7e24d05a4c31b1f9fc0" + "148189be60630edbbaa90a1c0cfeb5ff5b7a8fc11fa0e22fb27d3c3a689afac4"
			+ "641bbfee1b2a5d7afcb48449b3a58f493551056cf6d3a63393bee03b6f28db01";

	public Do_LoginController() throws Exception {

		byte[] modulus = Hex.decode(Modulus_hex.getBytes());
		byte[] publicExponent = Hex.decode(Public_exponent_hex.getBytes());

		byte[] privateExponent = Hex.decode(Private_exponent_hex.getBytes());

		priKey = RSAKey.generateRSAPrivateKey(modulus, privateExponent);

		System.out.println("Do_UserLoginController created .");

	}

	private void setRedirectToLogin(HttpServletResponse httpResponse, String nextUrl) throws IOException {
		//
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// params.add(new BasicNameValuePair("message","账号密码错误"));
		String errorMessage = "账号密码错误.";
		// errorMessage=errorMessage.replaceAll(" ", "%20");
		params.add(new BasicNameValuePair("message", errorMessage));
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
				accountText = new String(RSAKey.decrypt(priKey, Hex.decode(accountEncoded.getBytes())));
			}
		} catch (Exception e) {
			logger.error("decode account failed");
		}
		try {
			if (passwdEncoded != null && !"".equals(passwdEncoded)) {
				passwdText = new String(RSAKey.decrypt(priKey, Hex.decode(passwdEncoded.getBytes())));
			}
		} catch (Exception e) {
			logger.error("decode passwd failed");
		}
		try {
			if (new_passwd_1Encoded != null && !"".equals(new_passwd_1Encoded)) {
				newPasswdText = new String(RSAKey.decrypt(priKey, Hex.decode(new_passwd_1Encoded.getBytes())));
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
				setRedirectToLogin( httpResponse,"/user");
				return "user_jstl";
			}
					
	}
	
	@RequestMapping(value = "/do_login", method = { RequestMethod.POST, RequestMethod.GET })
	public String handleLogin(@RequestParam(required = false, value = "account") String accountEncoded, @RequestParam(required = false, value = "passwd") String passwdEncoded,
			@RequestParam(required = false, value = "nextUrl") String nextUrlEncoded, Model model, HttpSession session, HttpServletResponse httpResponse) throws Exception {

		Boolean login = (Boolean) session.getAttribute("login");
		String loginedAccount = (String) session.getAttribute("account");

		// already logined
		if (login != null && login == true) {
			model.addAttribute("account", loginedAccount);
			return "user_jstl";
		}

	
		// String accountCipher=accountEncoded;
		// String passwdCipher=passwdEncoded;

		String accountText = null;
		String passwdText = null;
		String nextUrl = null;

		try {
			if (accountEncoded != null && !"".equals(accountEncoded)) {
				accountText = new String(RSAKey.decrypt(priKey, Hex.decode(accountEncoded.getBytes())));
			}
		} catch (Exception e) {
			logger.error("decode account failed");
		}
		try {
			if (passwdEncoded != null && !"".equals(passwdEncoded)) {
				passwdText = new String(RSAKey.decrypt(priKey, Hex.decode(passwdEncoded.getBytes())));
			}
		} catch (Exception e) {
			logger.error("decode passwd failed");
		}

		try {
			if (nextUrlEncoded != null && !"".equals(nextUrlEncoded)) {
				nextUrl = new String(UrlBase64.decode(nextUrlEncoded.getBytes()));
			}
		} catch (Exception e) {
			logger.error("decode nextUrl failed");
		}

		logger.info("account:"+accountEncoded + "=>" + accountText);
		logger.info(" passwd:"+passwdEncoded + "=>" + passwdText);
		logger.info("nextUrl:"+nextUrlEncoded + "=>" + nextUrl);
		
		if (accountEncoded == null || passwdEncoded == null) {
			setRedirectToLogin(httpResponse, nextUrl);
			return "user_jstl";
		}
		

		boolean checkResult = UserAffairs.isPasswdRight(accountText, passwdText);

		logger.info("checkResult:" + checkResult);

		// 设置cookie
		// HttpServletResponse response.addCookie(new Cookie("foo", "bar"));
		if (!checkResult) {
			
			setRedirectToLogin(httpResponse, nextUrl);
			return "user_jstl";
			
		} else {
			//login success
			// 设置session
			logger.info("set session, login=true");
			
			session.setAttribute("login", true);
			session.setAttribute("account", accountText);

			if (nextUrl != null && !"".equals(nextUrl)) {
				httpResponse.sendRedirect(nextUrl);
				logger.info("Redirect to " + nextUrl);
			} else {
				
				model.addAttribute("account", accountText);
				
				return "user_jstl";
			}

		}

		return "user_jstl";
	}
}
