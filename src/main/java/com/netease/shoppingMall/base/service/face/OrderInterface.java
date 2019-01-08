package com.netease.shoppingMall.base.service.face;

import com.netease.shoppingMall.base.domain.Order;
import com.netease.shoppingMall.core.service.face.BaseInterface;

public interface OrderInterface extends BaseInterface<Order>{
	public int getOrderSum(Integer userId);
}
