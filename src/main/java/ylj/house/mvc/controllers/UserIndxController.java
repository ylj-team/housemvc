package ylj.house.mvc.controllers;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
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
import org.springframework.web.bind.annotation.RequestParam;

import ylj.house.tmsf.data.salestate.PropertyDailySigned;
import ylj.house.tmsf.data.salestate.PropertyDailySignedAffairs;
import ylj.house.tmsf.data2.DaySaledHouseAffairs;
import ylj.house.user.subscription.UserPropertySubscription;
import ylj.house.user.subscription.UserPropertySubscriptionAffairs;

@Controller("UserIndxController")
public class UserIndxController {

	static Logger logger = LoggerFactory.getLogger(UserIndxController.class);

	private void setRedirectToLogin(HttpServletResponse httpResponse, String nextUrl, String message) throws IOException {
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

	public static class SubscribedProperty extends UserPropertySubscription {

		String propertyId;

		public String getPropertyId() {
			return propertyId;
		}

		public void setPropertyId(String propertyId) {
			this.propertyId = propertyId;
		}

		public String getPropertyName() {
			return propertyName;
		}

		public void setPropertyName(String propertyName) {
			this.propertyName = propertyName;
		}

		String propertyName;
		int signNumber;

		public int getSignNumber() {
			return signNumber;
		}

		public void setSignNumber(int signNumber) {
			this.signNumber = signNumber;
		}

	}

	private int maxSignedNumber(List<PropertyDailySigned> signeds) {
		int maxSignedNumber = 0;
		for (PropertyDailySigned signed : signeds) {
			int signedNumber = Integer.parseInt(signed.getSignedNumber());
			if (signedNumber > maxSignedNumber)
				maxSignedNumber = signedNumber;
		}

		return maxSignedNumber;
	}

	@RequestMapping(value = "/userIdxFastPath", method = { RequestMethod.POST, RequestMethod.GET })
	public String handleUserFastPath(@RequestParam(value = "account", required = true) String account, HttpSession session, Model model,
			HttpServletResponse httpResponse) throws Exception {

		session.setAttribute("login", true);
		session.setAttribute("account", account);

		// already logined
		List<SubscribedProperty> subscriptions = new LinkedList<SubscribedProperty>();
		List<UserPropertySubscription> subs = UserPropertySubscriptionAffairs.getUserSubscriptions(account);

		if (subs.size() > 0) {
			SimpleDateFormat ISO_time_format = new SimpleDateFormat("yyyy-MM-dd");
			String dateStr = ISO_time_format.format(new Date(System.currentTimeMillis()));

			for (UserPropertySubscription sub : subs) {

				SubscribedProperty subscription = new SubscribedProperty();

				int signNumber = maxSignedNumber(PropertyDailySignedAffairs.querySaledHouseBetween(sub.getPropertyId(), dateStr, dateStr));

				subscription.setPropertyId(sub.getPropertyId());
				subscription.setPropertyName(sub.getPropertyName());
				subscription.setSignNumber(signNumber);

				subscriptions.add(subscription);
			}

		}
		
		model.addAttribute("account", account);
		model.addAttribute("subscriptions", subscriptions);

		return "user_index_jstl";
	}

	@RequestMapping(value = "/userIdx", method = { RequestMethod.POST, RequestMethod.GET })
	public String handleUser(HttpSession session, Model model, HttpServletResponse httpResponse) throws Exception {

		Boolean login = (Boolean) session.getAttribute("login");
		String loginedAccount = (String) session.getAttribute("account");

		// already logined
		if (login != null && login == true) {

			model.addAttribute("account", loginedAccount);

			List<SubscribedProperty> subscriptions = new LinkedList<SubscribedProperty>();
			List<UserPropertySubscription> subs = UserPropertySubscriptionAffairs.getUserSubscriptions(loginedAccount);

			if (subs.size() > 0) {
				SimpleDateFormat ISO_time_format = new SimpleDateFormat("yyyy-MM-dd");
				;
				String dateStr = ISO_time_format.format(new Date(System.currentTimeMillis()));

				for (UserPropertySubscription sub : subs) {

					SubscribedProperty subscription = new SubscribedProperty();

					int signNumber = maxSignedNumber(PropertyDailySignedAffairs.querySaledHouseBetween(sub.getPropertyId(), dateStr, dateStr));

					subscription.setPropertyId(sub.getPropertyId());
					subscription.setPropertyName(sub.getPropertyName());
					subscription.setSignNumber(signNumber);

					subscriptions.add(subscription);
				}

			}

			model.addAttribute("subscriptions", subscriptions);

			return "user_index_jstl";
		} else {
			setRedirectToLogin(httpResponse, "/userIdx", "未登陆");
			return null;
		}

	}
}
