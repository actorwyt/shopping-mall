package com.netease.shoppingMall.base.service.face;

import java.util.Map;

import com.netease.shoppingMall.base.domain.Goods;
import com.netease.shoppingMall.core.service.face.BaseInterface;

public interface GoodsInterface extends BaseInterface<Goods>{
	public Goods getGoods(Map<String,Object> map);
}
