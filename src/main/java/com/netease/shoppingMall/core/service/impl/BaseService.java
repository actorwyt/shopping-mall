package com.netease.shoppingMall.core.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.stereotype.Service;


import com.netease.shoppingMall.core.dao.face.BaseMapperInterface;
import com.netease.shoppingMall.core.service.face.BaseInterface;

@Service
public class BaseService<T> implements BaseInterface<T> {
	
	private BaseMapperInterface<T> baseMapper;

	public void setBaseMapper(BaseMapperInterface<T> baseMapper) {
		this.baseMapper = baseMapper;
	}
	
	@Override
	public List<T> getOnePage(Map<String,Object> map) {
		int currentPage = Integer.valueOf(map.get("currentPage").toString());
		int num = Integer.valueOf(map.get("num").toString());
		int start = (currentPage - 1) * num;
		int limit = num;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", start);
		params.put("limit", limit);
		if(map.get("userId") != null) {
			params.put("userId", Integer.valueOf(map.get("userId").toString()));
		}
		
		if(map.get("status") != null) {
			params.put("status", map.get("status"));
		}
		
		return baseMapper.getOnePage(params);
	}

	@Override
	public int count(Map<String, Object> params) {
		return baseMapper.count(params);
	}
	
	@Override
	public boolean exist(Map<String, Object> params) {
		T record = baseMapper.getOne(params);
		if(null != record ) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public int insert(T record) {
		return baseMapper.insert(record);
	}

	@Override
	public int update(T record) {
		return baseMapper.update(record);
	}

	@Override
	public int deleteOne(T record) {
		return baseMapper.deleteOne(record);
	}

	@Override
	public T getOne(Map<String, Object> params) {
		return baseMapper.getOne(params);
	}

	@Override
	public List<T> findList(Map<String, Object> params) {
		return baseMapper.findList(params);
	}

	@Override
	public int deletes(Map<String, Object> params) {
		return baseMapper.deletes(params);
	}
}
