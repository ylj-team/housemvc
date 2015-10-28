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
import ylj.security.passwd.PasswdEncodeStrategy;
import ylj.security.passwd.PasswdEncoder;

@Controller("Do_LoginController")
public class Do_LoginController {

	static Logger logger = LoggerFactory.getLogger(Do_LoginController.class);

	
	public Do_LoginController() throws Exception {

		System.out.println("Do_UserLoginController created .");

	}

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

	private void setRedirectToLoginPasswdChange(HttpServletResponse httpResponse,String account, String message) throws IOException {
		//
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// params.add(new BasicNameValuePair("message","璐﹀彿瀵嗙爜閿欒"));
	
		// errorMessage=errorMessage.replaceAll(" ", "%20");
		params.add(new BasicNameValuePair("account", account));
		params.add(new BasicNameValuePair("message", message));
		//params.add(new BasicNameValuePair("message", nextUrl));

		// URLEncode.encode 浼氬皢绌烘牸杞崲涓衡��+鈥濓紝浣嗘槸鈥�+鈥濆張涓嶅湪BBBBBBBBBBBBBBB璇存槑鐨�
		// 绌烘牸缂栫爜涓�"+" 鑰屼笉鏄� %20
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

		
		User user=UserAffairs.getUser(accountText);
		if(user==null){
			setRedirectToLoginPasswdChange(httpResponse,accountText, "账户不存在 "+accountText);
			logger.info("check old passwd failed ,account not exits"+accountText );
			return null;
		}
		
		PasswdEncoder	passwdEncoder=PasswdEncodeStrategy.getPasswdEncoder(user.getPasswdEncodeStrategy());
		
		if(passwdEncoder==null){
			setRedirectToLoginPasswdChange(httpResponse,accountText, "获取策略加密器失败："+user.getPasswdEncodeStrategy());
			logger.error("check old passwd  ,no Strategy find,Strategy："+user.getPasswdEncodeStrategy());
			return null;
		}
		
		String encodedPassed=passwdEncoder.passwdEncode(passwdText);
		
		if(!encodedPassed.equals(user.getPasswdEncoded())){
			setRedirectToLoginPasswdChange(httpResponse, accountText,"原密码不正确");
			logger.info("check old passwd ,password not match.");
			return null;
		}
		
		
		
	
			
			try{
				PasswdEncoder newPasswdEncoder=PasswdEncodeStrategy.getPasswdEncoder(PasswdEncodeStrategy.Strategy_E_1);
				String newPasswdEncoded=newPasswdEncoder.passwdEncode(newPasswdText);
				
				UserAffairs.newPasswd(accountText, PasswdEncodeStrategy.Strategy_E_1,newPasswdEncoded);
				
				setRedirectToLoginPasswdChange( httpResponse, accountText, "密码修改成功");
				
				logger.info("update new Passwd success ");
			}catch(Exception e){
				
				logger.error("update new Passwd failed ");
				setRedirectToLoginPasswdChange( httpResponse, accountText, "密码修改失败");
			}
			
	
			//@RequestParam(required = false, value = "account") String accountEncoded, @RequestParam(required = false, value = "passwd") String passwdEncoded,
	
		
		
		return null;
					
	}
	

	
	@RequestMapping(value = "/do_login", method = { RequestMethod.POST, RequestMethod.GET })
	public String handleLogin(@RequestParam("account") String accountEncoded, @RequestParam( "passwd") String passwdEncoded,
			@RequestParam(value="nextUrl",required=false) String nextUrlEncoded,@RequestParam(value="captchaToken",required=false) String captchaToken
			,Model model, HttpSession session, HttpServletResponse httpResponse) throws Exception {

		Boolean login = (Boolean) session.getAttribute("login");
		String loginedAccount = (String) session.getAttribute("account");

		// already logined
		if (login != null && login == true) {

			httpResponse.sendRedirect("./userIdx");
			logger.info("Redirect to userIdx" );
			return null;
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
			//setRedirectToLogin(httpResponse, null,"decode nextUrl failed");
			//return null;
			nextUrl=null;
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
		
	
		User user=UserAffairs.getUser(accountText);
		if(user==null){
			setRedirectToLogin(httpResponse, nextUrl,"账户不存在"+accountText);
			logger.info("login failed ,account not exits"+accountText );
			return null;
		}
		
		
		
		PasswdEncoder	passwdEncoder=PasswdEncodeStrategy.getPasswdEncoder(user.getPasswdEncodeStrategy());
		
		if(passwdEncoder==null){
			setRedirectToLogin(httpResponse, nextUrl,"获取策略加密器失败："+user.getPasswdEncodeStrategy());
			logger.error("login failed ,no Strategy find,Strategy："+user.getPasswdEncodeStrategy());
			return null;
		}
		
		String encodedPassed=passwdEncoder.passwdEncode(passwdText);
		
		if(!encodedPassed.equals(user.getPasswdEncoded())){
			setRedirectToLogin(httpResponse, nextUrl,"密码不正确");
			logger.info("login failed ,password not match.");
			return null;
		}
		
		
		
		if(user.getState()!=0){
			setRedirectToLogin(httpResponse, nextUrl,"用户状态可用 state:"+user.getState());
			logger.info("user state:"+user.getState());

			return null;
		}

		logger.info("login success :" + user.getAccount());

		
		// 璁剧疆session
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
			
			httpResponse.sendRedirect("./userIdx");
			logger.info("Redirect to userIdx" );
			return null;
		}

	

	}
}
