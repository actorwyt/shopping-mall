package com.netease.shoppingMall.base.service.impl;

import java.util.HashMap;
//import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.shoppingMall.base.dao.face.GoodsMapperInterface;
import com.netease.shoppingMall.base.domain.Goods;
import com.netease.shoppingMall.base.service.face.GoodsInterface;
import com.netease.shoppingMall.core.service.impl.BaseService;

@Service
public class GoodsService extends BaseService<Goods> implements GoodsInterface{
	@Resource
	private GoodsMapperInterface goodsMapper;

	@Autowired
	public void setBaseMapper() {
		super.setBaseMapper(goodsMapper);
	}

	@Override
	public Goods getGoods(Map<String, Object> map) {
		// TODO Auto-generated method stub
		Goods goods = goodsMapper.getOne(map);
		return goods;
	}
}
