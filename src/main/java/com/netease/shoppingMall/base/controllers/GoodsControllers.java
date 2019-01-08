package com.netease.shoppingMall.base.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.netease.shoppingMall.base.domain.Goods;
import com.netease.shoppingMall.base.service.face.GoodsInterface;
import com.netease.shoppingMall.base.service.face.OrderInterface;
import com.netease.shoppingMall.core.domain.User;
import com.netease.shoppingMall.core.util.FileUploadUtil;


@Controller
@RequestMapping("/goods")
public class GoodsControllers {

	@Resource
	private GoodsInterface goodsService;
	
	@Resource
	private OrderInterface orderService;
	
	
	/**
	 * 获得所有商品数量
	 * @param itemsOnPage
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/count")
	public int countGoods(@RequestParam("status") String status, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", status);
		//只统计有效商品数量
		map.put("isValid", "1");
		User user = (User)request.getSession().getAttribute("user");
		if(user != null) {
			map.put("userId", user.getUserId());
		}
		int total = goodsService.count(map);
		return total;
	}
	
	/**
	 * 获取所有商品
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@ResponseBody
	@RequestMapping("/getAllGoods")
	public List<Goods> getMyGoods(@RequestParam("currentPage") int currentPage, @RequestParam("itemsOnPage") int num, 
			@RequestParam("status") String status, HttpServletRequest request, HttpServletResponse response) {		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("isValid", "1");
		map.put("isCompleted", "1");
		map.put("status",status);
		User user = (User)request.getSession().getAttribute("user");
		if(user != null) {
			map.put("userId", user.getUserId());
		}	
		map.put("currentPage", currentPage);
		map.put("num", num);
		List<Goods> res = goodsService.getOnePage(map);
		return res;
	}
	
	@RequestMapping("/showGoodsPage")
	public String showGoodsPage(@RequestParam("goodsId") Integer goodsId, HttpSession session, HttpServletRequest request, ModelMap model) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("goodsId", goodsId);
		User user = (User)session.getAttribute("user");
		if(user != null && user.getRoleId().equals(1)) {
			map.put("userId", user.getUserId());
			model.addAttribute("isPurchased", orderService.exist(map));
		}	
		model.addAttribute("goodsId",goodsId);
		return "views/goods/goodsPage";
	}
	
	
	@ResponseBody
	@RequestMapping("/getOneGoods")
	public Goods getOneGoods(@RequestParam("goodsId") Integer goodsId, HttpSession session, HttpServletRequest request, ModelMap model) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("goodsId", goodsId);
		Goods goods = goodsService.getGoods(map);
		return goods;
	}
	
	
	/**
	 * 获取商品图片
	 * @param imgSrc
	 * @param request
	 * @param response
	 * @return
	 */
	
	@ResponseBody
	@RequestMapping("/showGoodsImage")
	public void showGoodsImage(@RequestParam("imgSrc") String imgSrc, HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("image/*");
		FileInputStream fis = null;
		OutputStream os = null;
		try{ 
			File img = FileUploadUtil.getFile(imgSrc);
			if(!img.isDirectory()){
				fis = new FileInputStream(img);
				os = response.getOutputStream();
				int count = 0;
				byte[] buffer = new byte[1024*8];
				while((count = fis.read(buffer)) != -1) {
					os.write(buffer, 0, count);
					os.flush();
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(fis != null) {
					fis.close();
				}
				if(os != null) {
					os.close();
				}				
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
