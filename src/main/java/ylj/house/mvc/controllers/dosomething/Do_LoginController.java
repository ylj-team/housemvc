package ylj.house.mvc.controllers.dosomething;


import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
	
	static Logger logger=LoggerFactory.getLogger(Do_LoginController.class);

	RSAPrivateKey priKey = null;
	
	  static String Modulus_hex ="00" +
	  "bbea718283a23953a59cf2f14c177a7d1bc82c6109b6e97f7a495c3869b1db1e" +
	  "546971de9a3a13d711b444deee67bb6c9f215d7e89ae3e1366b45ddef8be3c9a" +
	  "02920324e98d0f9dffc8414ce6e6e12b986c62fa60e5b4149ab0f693c2cc6816" +
	 "5dc861030adcdede7589d5ba1fa1cedf8bdf4d1565db7ee264ae37633621aeef";
	  
	  static String Public_exponent_hex = "010001";
	  
	  static String Private_exponent_hex ="" +
	  "02c2243c68363f652cef2ad9c3e62c541dce48687c3e051b6bee1bbe703ebe1a" +
	  "a9de8a5f5c20321e5c122b58a2633f6b0ec2ec9e68e2f7e24d05a4c31b1f9fc0" +
	  "148189be60630edbbaa90a1c0cfeb5ff5b7a8fc11fa0e22fb27d3c3a689afac4" +
	  "641bbfee1b2a5d7afcb48449b3a58f493551056cf6d3a63393bee03b6f28db01";
	
	  
	public Do_LoginController() throws Exception{
		
		byte[] modulus = Hex.decode(Modulus_hex.getBytes());
		byte[] publicExponent = Hex.decode(Public_exponent_hex.getBytes());
		
		byte[] privateExponent = Hex.decode(Private_exponent_hex.getBytes());
		

		
		 priKey = RSAKey.generateRSAPrivateKey(modulus, privateExponent);

		System.out.println("Do_UserLoginController created .");
		
	}
	@RequestMapping(value = "/do_login_jstl", method = RequestMethod.GET)
	public String handleLogin(@RequestParam("account") String account, @RequestParam("passwd") String passwd,@RequestParam(required=false,value="nextUrl") String nextUrlEncoded,Model model,HttpSession session,HttpServletResponse httpResponse) throws Exception {

		
		
		String accountCipher=account;
		String passwdCipher=passwd;

		String accountText =null;
		String passwdText =null;
		
		 try {
			accountText = new String(RSAKey.decrypt(priKey, Hex.decode(accountCipher.getBytes())));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 try {
			passwdText = new String(RSAKey.decrypt(priKey, Hex.decode(passwdCipher.getBytes())));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 String nextUrl=null;
		 if(nextUrlEncoded!=null){
			 nextUrl= new String(UrlBase64.decode( nextUrlEncoded.getBytes()));
		 }
		 
		logger.info(accountCipher+"=>"+accountText);
		logger.info(passwdCipher+"=>"+passwdText);
		logger.info(nextUrlEncoded+"=>"+nextUrl);
		
		boolean checkResult=UserAffairs.isPasswdRight(accountText,passwdText);
		
		System.out.println("checkResult:"+checkResult);
		
		model.addAttribute("account", accountText);
		model.addAttribute("passwd", passwdText);
		model.addAttribute("nextUrl", nextUrl);
		model.addAttribute("checkResult", checkResult);
		
		
		
		//设置cookie
		//HttpServletResponse response.addCookie(new Cookie("foo", "bar"));
		
		//设置session	
		if(checkResult){
			System.out.println("set session, login=true");		
			session.setAttribute("login", true);
			
			if(nextUrl!=null&&!"".equals(nextUrl)){
			
				httpResponse.sendRedirect(nextUrl);
				
			}
		}
		
		return "do_login_jstl";
	}
}
