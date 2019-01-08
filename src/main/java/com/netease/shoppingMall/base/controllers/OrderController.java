package com.netease.shoppingMall.base.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.netease.shoppingMall.base.domain.Order;
import com.netease.shoppingMall.base.service.face.OrderInterface;
import com.netease.shoppingMall.core.domain.User;

@Controller
@RequestMapping("/order")
public class OrderController {
	@Resource
	private OrderInterface orderService;
	
	@RequestMapping("/myOrder")
	public String myOrder(HttpSession session, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		User user = (User)session.getAttribute("user");
		if(user == null) {
			try {
				response.sendRedirect(request.getContextPath() + "/login");
				return null;
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		return "views/order/order";
	}
	
	/**
	 * 获得用户订单数量
	 * @param session
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/count")
	public int countOrderItems(HttpServletRequest request, HttpServletResponse response) {
		User user = (User)request.getSession().getAttribute("user");
		Map<String, Object> map = new HashMap<String, Object>();
		if(user != null) {
			map.put("userId", user.getUserId());
			int res = orderService.count(map);
			return res;
		} else {
			return 0;
		}
	}
	
	/**
	 * 获得用户所有订单
	 * @param currentPage
	 * @param itemsOnPage
	 * @param session
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getOrderGoods")
	public List<Order> getAllItems(@RequestParam("currentPage") int currentPage, @RequestParam("itemsOnPage") int num, 
			HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = (User)session.getAttribute("user");
		map.put("userId", user.getUserId());
		map.put("currentPage", currentPage);
		map.put("num", num);
		List<Order> res = orderService.getOnePage(map);
		return res;
	}
	
	
	/**
	 * 获得用户订单总价
	 * @param session
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getSum")
	public int getSum(HttpSession session, HttpServletRequest request) {
		Integer userId = session.getAttribute("user") == null ? 0 : ((User)session.getAttribute("user")).getUserId();
		return orderService.getOrderSum(userId);
	}
	
}
