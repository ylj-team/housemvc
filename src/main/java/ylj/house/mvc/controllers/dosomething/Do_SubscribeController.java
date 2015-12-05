package ylj.house.mvc.controllers.dosomething;


import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import ylj.house.tmsf.data.property.Property;
import ylj.house.tmsf.data.property.PropertyAffairs;
import ylj.house.user.subscription.UserPropertySubscription;
import ylj.house.user.subscription.UserPropertySubscriptionAffairs;

@Controller("Do_SubscribeController")
public class Do_SubscribeController {

	static Logger logger = LoggerFactory.getLogger(Do_SubscribeController.class);

	public Do_SubscribeController() throws Exception {

		System.out.println("Do_SubscribeController created .");

	}

	//./do_subscribe?propertyId=163840737
	
	
	@ResponseBody
	@RequestMapping(value = "/do_subscribe", method = RequestMethod.GET)
	public String handleSubscribe(@RequestParam("propertyId") String propertyId, Model model, HttpSession session) throws Exception {

		// 设置cookie
		// HttpServletResponse response.addCookie(new Cookie("foo", "bar"));
		String loginedAccount = (String) session.getAttribute("account");
		logger.info("loginedAccount:"+loginedAccount);
		JSONObject responseJSONObject = new JSONObject();

		try {
			// 设置session
			UserPropertySubscription subscription = UserPropertySubscriptionAffairs.getUserSubscription(loginedAccount, propertyId);
			if (subscription == null) {

				Property property = PropertyAffairs.getPropertyId(propertyId);

				subscription = new UserPropertySubscription();
				subscription.setAccount(loginedAccount);
				subscription.setPropertyId(propertyId);
				subscription.setSubscriptTime(System.currentTimeMillis());
				
				if (property != null)
					subscription.setPropertyName(property.getPropertyName());
				// subscription.
				logger.info("account:"+loginedAccount+",propertyId:"+propertyId+",propertyName:"+subscription.getPropertyName());
				
				
				UserPropertySubscriptionAffairs.createSubscription(subscription);
				responseJSONObject.put("success", "true");
				responseJSONObject.put("msg", "subscribe success");

			} else {
				responseJSONObject.put("success", "true");
				responseJSONObject.put("msg", "already subscribed");
			}
		} catch (Exception e) {
			responseJSONObject.put("success", "false");
			responseJSONObject.put("msg", "inner error. excepion:"+e);
			logger.error("",e);
		}

		return JSON.toJSONString(responseJSONObject);
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/do_unsubscribe", method = RequestMethod.GET)
	public String handleUnsubscribe(@RequestParam("propertyId") String propertyId, Model model, HttpSession session) throws Exception {

		// 设置cookie
		// HttpServletResponse response.addCookie(new Cookie("foo", "bar"));
		String loginedAccount = (String) session.getAttribute("account");

		JSONObject responseJSONObject = new JSONObject();

		try {
			// 设置session
			UserPropertySubscription subscription = UserPropertySubscriptionAffairs.getUserSubscription(loginedAccount, propertyId);
			if (subscription == null) {
				responseJSONObject.put("success", "true");
				responseJSONObject.put("msg", "not subscribed");
				
			} else {
				UserPropertySubscriptionAffairs.deleteSubscription(loginedAccount, propertyId);
				responseJSONObject.put("success", "true");
				responseJSONObject.put("msg", "unsubscribe success");
			}
		} catch (Exception e) {
			responseJSONObject.put("success", "false");
			responseJSONObject.put("msg", "inner error. excepion:"+e);
			logger.error("",e);
		}

		return JSON.toJSONString(responseJSONObject);
	}
}
