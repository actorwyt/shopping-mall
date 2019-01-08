package com.netease.shoppingMall.base.controllers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.netease.shoppingMall.base.domain.Goods;
import com.netease.shoppingMall.base.service.face.GoodsInterface;
import com.netease.shoppingMall.base.service.face.SellerInterface;
import com.netease.shoppingMall.core.domain.User;


@Controller
@RequestMapping("seller")
public class SellerController {
	@Resource
	private SellerInterface sellerService;
	
	@Resource
	private GoodsInterface goodsService;
	
	/**
	 * 获取我的商品列表 主页
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping("/getMyGoods")
	public String getMyGoodsPage(HttpSession session, HttpServletRequest request) {
		return "/views/goods/showGoods";
	}
	
	/**
	 * 获取我的商品数量
	 * @param status
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/count")
	public int count(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = (User)request.getSession().getAttribute("user");
		if(user != null) {
			map.put("userId", user.getUserId());
			return goodsService.count(map);
		} else {
			return 0;
		}
		
	}
	
	
	/**
	 * 获取我的商品列表
	 * @param currentPage
	 * @param itemsOnPage
	 * @param session
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getAll")
	public List<Goods> getGoodsItems(@RequestParam("currentPage") int currentPage, @RequestParam("itemsOnPage") int num, 
			HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = (User)session.getAttribute("user");
		map.put("currentPage", currentPage);
		map.put("num", num);
		map.put("userId", user.getUserId());
		List<Goods> res = goodsService.getOnePage(map);
		return res;
	}
	
	/**
	 * 新建，编辑商品
	 * @param goodsId
	 * @param request
	 * @param model
	 * @return 视图页面
	 */
	@RequestMapping("/editGoods")
	public String editGoodsPage(@RequestParam("goodsId") Integer goodsId, HttpServletRequest request, ModelMap model) {
		model.addAttribute("goodsId" , goodsId);
		return "/views/goods/editGoods";
	}
	
	/**
	 * 新建，编辑商品 保存
	 * @param goodsInfo
	 * @param status
	 * @param session
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/editGoods/{status}", method = RequestMethod.POST) 
	public Map<String, Object> editGoods(@RequestBody Goods goods, @PathVariable("status") String status, HttpSession session, HttpServletRequest request) {
		Integer sellerId = session.getAttribute("user") == null ? 0 : ((User)session.getAttribute("user")).getUserId();
		goods.setSellerId(sellerId);
		return sellerService.releaseOrEditGoods(goods, status);
	}
	
	/**
	 * 上传商品图片
	 * @param file
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
	public Map<String, Object> uploadImage(@RequestParam(value = "goodsImage", required = false) MultipartFile goodsImage, 
			@RequestParam("fileType") String fileType, HttpServletRequest request, HttpServletResponse response) throws IOException {	
		Integer sellerId = ((User)request.getSession().getAttribute("user")).getUserId();
		Map<String, Object> res = new HashMap<String, Object>();
		response.setCharacterEncoding("utf-8");		
		response.setContentType("text/html; charset=utf-8");
		String pathToSave = File.separator + sellerId ;
		res = sellerService.uploadImage(goodsImage, pathToSave, sellerId);	
		return res;
	}
	
	
	/**
	 * 卖家删除商品，若商品已存在于订单或购物车中，则标记为无效
	 * @param goodsId
	 * @param session
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteMyGoods", method = RequestMethod.POST)
	public Map<String, Object> deleteMyGoods(@RequestBody Goods goods, HttpSession session, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = (User)session.getAttribute("user");
		Integer sellerId = user == null ? 0 : user.getUserId();
		goods.setSellerId(sellerId);
		return sellerService.deleteGoods(goods);
	}
}
