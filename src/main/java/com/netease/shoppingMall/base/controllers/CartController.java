package com.netease.shoppingMall.base.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.netease.shoppingMall.base.domain.Goods;
import com.netease.shoppingMall.base.service.face.CartInterface;
import com.netease.shoppingMall.core.domain.User;

@Controller
@RequestMapping("/shoppingCart")
public class CartController {

	@Resource
	private CartInterface cartService;
	
	@RequestMapping("/cart")
	public String shoppingCart(HttpSession session,ModelMap model) {
		model.addAttribute("userId", ((User)session.getAttribute("user")).getUserId());
		return "/views/shoppingCart/cart";
	}
	
	/**
	 * 获得用户购物车项目数量
	 * @param session
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/countCartGoods")
	public int count(@RequestParam(value = "status",required = false) String status, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = (User)request.getSession().getAttribute("user");
		if(user == null) {
			return 0;
		}
		else {
			Integer userId = user.getUserId();
			map.put("userId", userId);
			if(userId != 0) {
				map.put("userId", userId);
				map.put("status", status);
				int total = cartService.count(map);
				return total;
			} else {
				return 0;
			}
		}

	}
	
	/**
	 * 获得用户购物车中所有商品
	 * @param currentPage
	 * @param itemsOnPage
	 * @param session
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getCartGoods")
	public List<Map<String, Object>> getCartGoods(@RequestParam("currentPage") int currentPage, @RequestParam("itemsOnPage") int num, 
			HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer userId = session.getAttribute("user") == null ? 0 : ((User)session.getAttribute("user")).getUserId();
		map.put("currentPage", currentPage);
		map.put("num", num);
		map.put("userId", userId);
		List<Map<String,Object>> res = cartService.getOnePage(map);
		return res;
	}
	
	/**
	 * 添加商品入购物车
	 * @param session
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addToCart", method = RequestMethod.POST)
	public String addToCart(@RequestBody Map<String, Object> params, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		Integer userId = session.getAttribute("user") == null ? 0 : ((User)session.getAttribute("user")).getUserId();
		params.put("userId", userId);
		return cartService.addToCart(params);
	}
	
	/**
	 * 删除购物车中项目
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteGoods" ,method = RequestMethod.POST)
	public Map<String, Object> deleteGoods(@RequestBody Goods goods, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> res = new HashMap<String, Object>();
		map.put("goodsId", goods.getGoodsId());
		map.put("userId", ((User)request.getSession().getAttribute("user")).getUserId());
		if(cartService.deleteOne(map) > 0) {
			res.put("status", "success");
			res.put("msg", "删除成功！");
		} else {
			res.put("status", "fail");
			res.put("msg", "删除失败！");
		}
		return res;
	}
	
	/**
	 * 结算
	 * @param status
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkout", method = RequestMethod.POST)
	public String checkout(@RequestBody Map<String, Object> goods, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("goodsIds", goods.get("goodsIds"));
		map.put("userId", ((User)request.getSession().getAttribute("user")).getUserId());
		return cartService.checkout(map);
	}
}
