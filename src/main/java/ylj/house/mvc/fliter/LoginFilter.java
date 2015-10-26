package ylj.house.mvc.fliter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bouncycastle.util.encoders.UrlBase64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ylj.house.mvc.interceptor.AuthorizationCheckInterceptor;

public class LoginFilter implements Filter {

	static Logger logger = LoggerFactory.getLogger(LoginFilter.class);

	private List<String> excludedUrls = new LinkedList<String>();

	FilterConfig filterConfig;

	public LoginFilter() {

	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;

		String excludedUrlsStr = filterConfig.getInitParameter("excludedUrls");
		if (excludedUrlsStr == null)
			return;

		String[] excludedUrlsArray = excludedUrlsStr.split(",");
		for (String excludedUrl : excludedUrlsArray) {
			this.excludedUrls.add(excludedUrl);
		}
		logger.info("excludedUrls:" + this.excludedUrls);
	}

	public boolean isResourceUrl(String uri){
		if(uri.endsWith(".js")){
			return true;
		}
		
		if(uri.endsWith(".jpg")){
			return true;
		}
		
		return false;
	}
	
	/*
	 * 示例：

当前url：http://localhost:8080/CarsiLogCenter_new/idpstat.jsp?action=idp.sptopn

request.getRequestURL() http://localhost:8080/CarsiLogCenter_new/idpstat.jsp
request.getRequestURI() /CarsiLogCenter_new/idpstat.jsp
request.getContextPath()/CarsiLogCenter_new
request.getServletPath() /idpstat.jsp

request.getQueryString()action=idp.sptopn

	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	// HttpServletRequest request, HttpServletResponse response
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		boolean pass = false;
		String uri = httpRequest.getRequestURI().toString();
		
		if(isResourceUrl(uri)){
		//	logger.info("resource URI=" + url + " pass:" + pass);
		//	return ;
			 pass=true;
		}
	
		// System.out.println(">>>: " + url);
		for (String s : excludedUrls) {
			if (uri.endsWith(s)) {
				pass = true;
				break;
			}
		}
		if (!pass) {
			Cookie[] cookies = httpRequest.getCookies();
			if (cookies != null) {
				for (int i = 0; i < cookies.length; i++) {

				//	System.out.println("cookie [" + i + "] " + cookies[i].getName() + ":" + cookies[i].getValue() + " Domain=" + cookies[i].getDomain() + " Path="
				//			+ cookies[i].getPath() + " Comment=" + cookies[i].getComment() + " MaxAge=" + cookies[i].getMaxAge() + " Version=" + cookies[i].getVersion());
					if (cookies[i].getName().equals("")) {
						break;
					}
				}
			} else {
				//System.out.println("cookies=null");
			}
		}
		// if (!pass) {

		HttpSession session = httpRequest.getSession();
		int sessionCounter=0;
		Enumeration<String> attributeNames = session.getAttributeNames();
		while (attributeNames.hasMoreElements()) {
			String attributeName = attributeNames.nextElement();
			//System.out.println("seesion." + attributeName + ":" + session.getAttribute(attributeName));
			sessionCounter++;
		}

		Object login = (Object) session.getAttribute("login");
		if (login != null) {
			pass = true;
		//	System.out.println("seesion.login ok");
		}
	//	System.out.println("seesion.size()="+sessionCounter);
		// }

		// pass=true;
		pass = true;
		
		logger.info("拦截 URI=" + httpRequest.getRequestURI() + " pass:" + pass);

		if (!pass) {
			logger.info(" redirect to login.html");
		//	System.out.println("throw Authorization failed. redirect to login.html");
			
			// throw new AuthorizationException();
			String url=uri+"?"+httpRequest.getQueryString();
			String urlEncoded=new String(UrlBase64.encode(url.getBytes()));		
			
			String location = "./login.html?nextUrl="+urlEncoded;
			logger.info(" redirect to :"+location);
			
			httpResponse.sendRedirect(location);
		} else {
			chain.doFilter(httpRequest, httpResponse);
		}

	}

	public void destroy() {
		// TODO Auto-generated method stub

	}

}
