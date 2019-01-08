package com.netease.shoppingMall.core.service.face;

import java.util.List;
import java.util.Map;

public interface BaseInterface<T> {
	public List<T> getOnePage(Map<String,Object> map);
	
	public int count(Map<String, Object> map);
	
	public boolean exist(Map<String, Object> map);
	
	public T getOne(Map<String, Object> map);
	
	public List<T> findList(Map<String, Object> map);
		
	public int insert(T record);
		
	public int update(T record);
		
	public int deleteOne(T record);
		
	public int deletes(Map<String, Object> map);
}
