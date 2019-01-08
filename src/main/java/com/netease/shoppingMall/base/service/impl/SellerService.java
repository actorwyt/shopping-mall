package com.netease.shoppingMall.base.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.netease.shoppingMall.base.dao.face.CartMapperInterface;
import com.netease.shoppingMall.base.dao.face.GoodsMapperInterface;
import com.netease.shoppingMall.base.dao.face.OrderMapperInterface;
import com.netease.shoppingMall.base.domain.Goods;
import com.netease.shoppingMall.base.service.face.SellerInterface;
import com.netease.shoppingMall.core.util.FileUploadUtil;

@Service
public class SellerService implements SellerInterface{

	@Resource
	private GoodsMapperInterface goodsMapper;
	
	@Resource
	private CartMapperInterface cartMapper;
	
	@Resource
	private OrderMapperInterface orderMapper;
	
	
	private @Value("${goods.sizeLimit}") long UPLOAD_IMAGE_SIZE ;
	
	@Override
	public Map<String, Object> releaseOrEditGoods(Goods goods, String status) {
		// TODO Auto-generated method stub
		Map<String, Object> res = new HashMap<String, Object>();	
		if("release".equals(status)) {
			//发布商品
			if(goodsMapper.insert(goods) > 0) {
				res.put("result", "success");				
				res.put("msg", "发布成功！");
				res.put("goodsId", goods.getGoodsId());
			} else {
				res.put("result", "fail");
				res.put("msg", "发布失败！");
			}
		} else {
			//修改商品
			if(goodsMapper.update(goods) > 0) {
				res.put("result", "success");
				res.put("msg", "保存成功！");
				res.put("goodsId", goods.getGoodsId());
			} else {
				res.put("result", "fail");
				res.put("msg", "保存失败！");
			}
		}		
		return res;
	}

	@Override
	public Map<String, Object> deleteGoods(Goods goods) {
		// TODO Auto-generated method stub
		Map<String, Object> res = new HashMap<String, Object>();
		//检查商品是否已被存在于订单中
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("goodsId", goods.getGoodsId());
		int exist = orderMapper.count(params) + cartMapper.count(params);
		if(exist > 0) {
			//商品设为无效
			goods.setIsValid("0");
			if(goodsMapper.update(goods) > 0) {
				res.put("status", "success");
				res.put("msg", "商品已存在于订单或购物车中，已被标记为无效！");
				return res;
			}
		} else {
			if(goodsMapper.deleteOne(goods) > 0){
				res.put("status", "success");
				res.put("msg", "商品删除成功！");
				return res;
			}
		}
		
		res.put("status", "fail");
		res.put("msg", "商品删除失败！");
		return res;		
	}

	@Override
	public Map<String, Object> uploadImage(MultipartFile goodsImage, String pathToSave, Integer sellerId) {
		// TODO Auto-generated method stub
		Map<String, Object> res = new HashMap<String, Object>();
		try {
			if(goodsImage.getSize() > UPLOAD_IMAGE_SIZE) {
				res.put("status", "fail");
				res.put("msg", "文件大小超过标准！");
			} else {
				String newFileName = sellerId + "_" + new Date().getTime();
				String savedImgPath = FileUploadUtil.uploadFile(goodsImage, pathToSave, newFileName);
				if(savedImgPath != null) {
					res.put("status", "success");
					res.put("msg", "上传成功！");
					res.put("path", savedImgPath);
				} else {
					res.put("status", "fail");
					res.put("msg", "上传失败！");
				} 
			}
			return res;
		} catch(IOException e) {
			e.printStackTrace();
			res.put("status", "fail");
			res.put("msg", "上传失败");
			return res;
		}
	}

}
