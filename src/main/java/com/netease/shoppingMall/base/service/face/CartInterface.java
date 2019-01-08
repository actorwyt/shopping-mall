package com.netease.shoppingMall.base.service.face;

import java.util.Map;

import com.netease.shoppingMall.core.service.face.BaseInterface;

public interface CartInterface extends BaseInterface<Map<String, Object>>{
	public String checkout(Map<String, Object> map);
	
	public String addToCart(Map<String, Object> map);
}
