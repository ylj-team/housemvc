package ylj.house.mvc.controllers.dosomething;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.bouncycastle.jcajce.provider.digest.MD5;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ylj.house.user.Applicant;
import ylj.house.user.ApplicantAffairs;
import ylj.house.user.UserAffairs;
import ylj.mail.MailSender;
import ylj.security.passwd.PasswdEncodeStrategy;
import ylj.security.passwd.PasswdEncoder;

@Controller("Do_RegistController")
public class Do_RegistController {
	
	
	static Logger logger=LoggerFactory.getLogger(Do_RegistController.class);

	static String SALT="regist";
	
	MailSender mailSender =null;
	public Do_RegistController() throws Exception{
		
		System.out.println("Do_RegistController created .");
		
		System.out.println("MailSender init ...");			
		
		String smtpHost = "smtp.163.com";
		// 鍙戜欢浜虹殑璐﹀彿
		String mailUser = "yanglujun2007510@163.com";
		// 璁块棶SMTP鏈嶅姟鏃堕渶瑕佹彁渚涚殑瀵嗙爜
		String mailPassword = "405628494";
				
		mailSender = new MailSender();
		
		mailSender.init(smtpHost, mailUser, mailPassword);
		
	}
	private void setRedirectToRegPage(HttpServletResponse httpResponse, String message) throws IOException {
		//
		List<NameValuePair> params = new ArrayList<NameValuePair>();
	
		params.add(new BasicNameValuePair("message", message));
		String query = URLEncodedUtils.format(params, "UTF-8");
		query = query.replace("+", "%20");

		String reloginUrl = "./regist.html?" + query;
		logger.info("Redirect to " + reloginUrl);

		httpResponse.sendRedirect(reloginUrl);
	}
	
	@RequestMapping(value = "/do_regist", method = { RequestMethod.POST, RequestMethod.GET })
	public String handleRegist(@RequestParam("account") String accountEncoded,
			@RequestParam("passwd") String passwdEncoded,@RequestParam(value="captchaToken" ,required=false) String captchaToken,
			HttpSession session, Model model,HttpServletRequest httpRequest,HttpServletResponse httpResponse) throws Exception {
		

		

		String captchaTokenInSession=(String)session.getAttribute("captchaToken");
		if(captchaTokenInSession!=null){
			if(!captchaTokenInSession.equals(captchaToken)){
				setRedirectToRegPage(httpResponse,"验证码错误");
				logger.info("captchaToken check failed." );
				return null;
			}else{
				logger.info("captchaToken check success." );
			}
		}else{
			logger.info("captchaToken not set." );
		}
		
		
		
		String accountText = null;
		String passwdText = null;
		
		logger.info("accountEncoded:"+accountEncoded);
		logger.info(" passwdEncoded:"+passwdEncoded);
		
		if (accountEncoded==null) {
			logger.error("accountEncoded==null");
			setRedirectToRegPage(httpResponse,"accountEncoded==null");
			return null;
		}
		if (passwdEncoded==null) {
			logger.error("passwdEncoded==null");
			setRedirectToRegPage(httpResponse,"passwdEncoded==null");
			return null;
		}	
		
		
		try {
			if (accountEncoded != null && !"".equals(accountEncoded)) {
				accountText = new String(RSAUtils.decrypt(Hex.decode(accountEncoded.getBytes())));
			}
		} catch (Exception e) {
			logger.error("decode account failed");
			setRedirectToRegPage(httpResponse,"decode account failed");
			return null;
		}
		try {
			if (passwdEncoded != null && !"".equals(passwdEncoded)) {
				passwdText = new String(RSAUtils.decrypt( Hex.decode(passwdEncoded.getBytes())));
			}
		} catch (Exception e) {
			logger.error("decode passwd failed");
			setRedirectToRegPage(httpResponse,"decode passwd failed");
			return null;
		}
	
		if (!accountText.matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+")) {
			logger.error("email format error, account:"+accountText);
			setRedirectToRegPage(httpResponse,"email format error, account:"+accountText);
			return null;
		}
		
	
		if(UserAffairs.isAccountExists(accountText)){
			logger.error("User Account is Exists:"+accountText);
			setRedirectToRegPage(httpResponse,accountText+"  is Exists.");
			return null;
		}

		/*
		if(ApplicantAffairs.isAccountExists(accountText)){
			logger.error("Applicant Account is Exists:"+accountText);
			setRedirectToRegPage(httpResponse,accountText+"  is Exists.");
			return null;
		}
		*/
		String passwdEncodeStrategy=PasswdEncodeStrategy.Strategy_E_1;
		PasswdEncoder passwdEncoder=PasswdEncodeStrategy.getPasswdEncoder(passwdEncodeStrategy);
		String passwdEncodedInDB=passwdEncoder.passwdEncode(passwdText);

		
		String applyCode=ApplicantAffairs.generateApplyCode();
		
		Applicant applicant=ApplicantAffairs.getApplicant(accountText);
		boolean exitApplicant=false;
		
		if(applicant==null){
			applicant = new Applicant();
			applicant.setAccount(accountText);
			exitApplicant=false;
		}else{		
			exitApplicant=true;
		}
		
	
		applicant.setPasswdEncoded(passwdEncodedInDB);
		applicant.setPasswdEncodeStrategy(passwdEncodeStrategy);
		applicant.setApplyCode(applyCode);
		applicant.setApplyTime(System.currentTimeMillis());
		applicant.setActivated(false);
		applicant.setActivatedTime(-1);
		applicant.setApplyCodeMethod("EMAIL");
		//send appcode to account email
		//
		String url=httpRequest.getRequestURL().toString();
		logger.info("RequestURL:"+url);
		
		String acticeUrl=url.replace("do_regist", "do_regist_active");
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
	//	MD5.Digest.
		
		String applyCodeEncoded=DigestUtils.md5Hex(applyCode+"."+SALT);
	
		params.add(new BasicNameValuePair("account", accountText));
		params.add(new BasicNameValuePair("applyCode", applyCodeEncoded));
	//	params.add(new BasicNameValuePair("applyCodeEncoded", applyCodeEncoded));
		
		String query = URLEncodedUtils.format(params, "UTF-8");
		query = query.replace("+", "%20");
		
		acticeUrl=acticeUrl+"?"+query;
		
		logger.info("acticeUrl:"+acticeUrl);
		
		String mailAddress=accountText;
		String subject="激活邮件";
		String content = "<a href='"+acticeUrl+"'>"+acticeUrl+"</a>";
		String contentType="text/html;charset=UTF-8";
		
		boolean sendOk=true;
		try{
			mailSender.send(mailAddress, subject, content, contentType);
			logger.info(" send active email success. to:"+accountText);
			
		}catch(Exception e){
			e.printStackTrace();
			sendOk=false;
		}
		
		
		if(sendOk){
			if(exitApplicant){
				ApplicantAffairs.modfiyApplicant(applicant);
			}else{
				ApplicantAffairs.createApplicant(applicant);		
			}
				
			setRedirectToRegPage(httpResponse," send active email success. to:"+accountText);		
		}else{
			logger.error(" send active email failed. to:"+accountText);
			setRedirectToRegPage(httpResponse," send active email failed. to:"+accountText);					
		}
		
		return null;
					
	}
	@RequestMapping(value = "/do_regist_active", method = { RequestMethod.POST, RequestMethod.GET })
	public String handleRegistActive(@RequestParam("account") String account,
			@RequestParam("applyCode") String applyCodeEncoded,	
			HttpSession session, Model model,HttpServletResponse httpResponse) throws Exception {
		
		
		Applicant userApplicant=ApplicantAffairs.getApplicant(account);
		if(userApplicant==null){
			setRedirectToRegPage( httpResponse, "激活失败，未申请账户："+account);
			return null;
		}
		
		if(userApplicant.isActivated()){
			setRedirectToRegPage( httpResponse, "已激活账户："+account);
			return null;
		}
		
	
		String expectApplyCodeEncoded=	DigestUtils.md5Hex(userApplicant.getApplyCode()+"."+SALT);
		
		if(expectApplyCodeEncoded.equals(applyCodeEncoded)){
			ApplicantAffairs.activateUser(account);
			setRedirectToLogin( httpResponse, "激活成功"+account);
			return null;
		}else{
			 setRedirectToRegPage( httpResponse, "激活失败，激活码不正确： "+account);
			return null;
		}
		
	}
	private void setRedirectToLogin(HttpServletResponse httpResponse, String message) throws IOException {
		//
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// params.add(new BasicNameValuePair("message","璐﹀彿瀵嗙爜閿欒"));

		// errorMessage=errorMessage.replaceAll(" ", "%20");
		params.add(new BasicNameValuePair("message", message));


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

		String reloginUrl = "./login.html?" + query;
		logger.info("Redirect to " + reloginUrl);

		httpResponse.sendRedirect(reloginUrl);
	}

	
}
