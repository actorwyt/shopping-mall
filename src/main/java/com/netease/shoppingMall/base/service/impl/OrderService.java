package com.netease.shoppingMall.base.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.shoppingMall.base.dao.face.OrderMapperInterface;
import com.netease.shoppingMall.base.domain.Order;
import com.netease.shoppingMall.base.service.face.OrderInterface;
import com.netease.shoppingMall.core.service.impl.BaseService;

@Service
public class OrderService extends BaseService<Order> implements OrderInterface{
	@Resource
	private OrderMapperInterface orderMapper;
	
	@Autowired
	public void setBaseMapper() {
		super.setBaseMapper(orderMapper);
	}

	@Override
	public int getOrderSum(Integer userId) {
		// TODO Auto-generated method stub
		return orderMapper.getOrderSum(userId);
	}
}
