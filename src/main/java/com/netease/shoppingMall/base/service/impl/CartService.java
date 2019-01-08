package com.netease.shoppingMall.base.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.shoppingMall.base.dao.face.CartMapperInterface;
import com.netease.shoppingMall.base.dao.face.GoodsMapperInterface;
import com.netease.shoppingMall.base.dao.face.OrderMapperInterface;
import com.netease.shoppingMall.base.domain.Goods;
import com.netease.shoppingMall.base.service.face.CartInterface;
import com.netease.shoppingMall.core.service.impl.BaseService;

@Service
public class CartService extends BaseService<Map<String, Object>> implements CartInterface{

	@Resource
	private CartMapperInterface cartMapper;
	
	@Resource
	private GoodsMapperInterface goodsMapper;
	
	@Resource
	private OrderMapperInterface orderMapper;
	
	@Autowired
	public void setBaseMapper() {
		super.setBaseMapper(cartMapper);
	}

	@Override
	public String checkout(Map<String, Object> params) {
		// TODO Auto-generated method stub
		List<Integer> goodsIds = new ArrayList<Integer>();
		for(Object goodsId : (List<Object>)params.get("goodsIds")) {
			goodsIds.add(Integer.parseInt(goodsId.toString()));
		}
		Map<Integer, String> invalidGoodsList = new HashMap<Integer, String>();
		StringBuffer res = new StringBuffer("");
		if(!goodsIds.isEmpty()) {
			//首先检查所选商品库存是否充足
			for(Integer goodsId : goodsIds) {
				params.put("goodsId", goodsId);
				Goods goodsInfo = goodsMapper.getOne(params);
				Map<String, Object> cartItem = cartMapper.getOne(params);
				if(goodsInfo != null) {
					//商品已被商家标为无效
					if(!"1".equals(goodsInfo.getIsValid())) {
						invalidGoodsList.put(goodsId, "商品已失效！");
					} else if(goodsInfo.getAmount() < Integer.parseInt(cartItem.get("purchasedAmount").toString())) {
						invalidGoodsList.put(goodsId, "商品库存不足！");
					}					
				}
			}
			if(invalidGoodsList.isEmpty()) {
				orderMapper.checkout(params);
				goodsMapper.updateAfterPurchased(params);
				cartMapper.deletes(params);	
				res.append("{\"status\":\"success\",\"msg\":\"购买成功！\"}");
			} else {
				res.append("{\"status\":\"fail\"");
				res.append(",\"invalidGoodsList\":[");
				for(Integer invalidGoodsId : invalidGoodsList.keySet()) {
					res.append("{\"goodsId\":" + invalidGoodsId + ",\"msg\":\"" + invalidGoodsList.get(invalidGoodsId) + "\"},");
				}
				res.deleteCharAt(res.length()-1);
				res.append("]}");
			}
			
		} else {
			res.append("{\"status\":\"fail\",\"msg\":\"购买失败！\"}");
		}
		return res.toString();
   
	}

	@Override
	public String addToCart(Map<String, Object> map) {
		// TODO Auto-generated method stub
		String res = "";
		try {
			Goods goods = goodsMapper.getOne(map);
			if(goods.getAmount() < Integer.parseInt(map.get("purchasedAmount").toString())) {
				//判断商品是否有库存
				res = "{\"status\":\"fail\",\"msg\":\"商品库存不足！\"}";
			} else {
				Map<String, Object> cartItemExist = cartMapper.getOne(map);
				int amountToPurchase =  Integer.parseInt(map.get("purchasedAmount").toString());
				if(cartItemExist != null) {
					int amountExist = Integer.parseInt(cartItemExist.get("purchasedAmount").toString());
					//检查购物车中该商品的总数量是否会超过商品库存
					if((amountExist + amountToPurchase) > goods.getAmount()) {
						res = "{\"status\":\"fail\",\"msg\":\"购物车中该商品总数不能超过库存总数！\"}";
					} else if(cartMapper.update(map) > 0) {
						res = "{\"status\":\"success\",\"msg\":\"添加购物车成功！\"}";
					} else {
						res = "{\"status\":\"success\",\"msg\":\"添加购物车失败！\"}";
					}
				} else {	
					if(cartMapper.insert(map) > 0) {
						res = "{\"status\":\"success\",\"msg\":\"添加购物车成功！\"}";
					} else {
						res = "{\"status\":\"fail\",\"msg\":\"添加购物车失败！\"}";
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			return "{\"status\":\"fail\",\"msg\":\"添加购物车失败！\"}";
		}
		
		return res;
	}
}
