package com.netease.shoppingMall.base.dao.face;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.netease.shoppingMall.base.domain.Order;
import com.netease.shoppingMall.core.dao.face.BaseMapperInterface;

@Repository
public interface OrderMapperInterface extends BaseMapperInterface<Order>{
	public int getOrderSum(Integer userId);
	
	public int checkout(Map<String, Object> map);
}
