package com.netease.shoppingMall.base.dao.face;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.netease.shoppingMall.base.domain.Goods;
import com.netease.shoppingMall.core.dao.face.BaseMapperInterface;

@Repository
public interface GoodsMapperInterface extends BaseMapperInterface<Goods>{
	//用户购买商品后更新商品数量
	public int updateAfterPurchased(Map<String, Object> map);
}
