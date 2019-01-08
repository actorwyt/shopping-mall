package com.netease.shoppingMall.base.service.face;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.netease.shoppingMall.base.domain.Goods;

public interface SellerInterface {
	public Map<String, Object> releaseOrEditGoods(Goods goodsInfo, String status);
	
	public Map<String, Object> deleteGoods(Goods goods);
	
	public Map<String, Object> uploadImage(MultipartFile goodsImage, String pathToSave, Integer sellerId);
}
