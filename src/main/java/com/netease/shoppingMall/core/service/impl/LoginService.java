package com.netease.shoppingMall.core.service.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


import com.netease.shoppingMall.core.dao.face.SpringJdbcInterface;

import com.netease.shoppingMall.core.domain.User;
import com.netease.shoppingMall.core.service.face.LoginInterface;


/**
 * 登录服务接口，用于验证登录
 */
@Service
public class LoginService implements LoginInterface{
	
	@Autowired
    public SpringJdbcInterface springJdbcTemplate;
	
	@Override
	public int doLogin(User user, HttpSession session) {
		String username = user.getUsername() == null ? "" : user.getUsername().toString();
		String password = user.getPassword() == null ? "" : user.getPassword().toString();
		String sql = "select userId, username, password, roleId from netease_userinfo where username='" + username +"'";
		List<Map<String, Object>> userList = springJdbcTemplate.queryForList(sql);
		if(CollectionUtils.isEmpty(userList)) {
			return 1;        //用户不存在
		} else {
			Map<String, Object> map = userList.get(0);
			String passwd = map.get("password").toString();
			if(!passwd.equals(password)) {
				return 2;    //密码错误
			} else {
				//session中保存用户信息
				Integer userId = (Integer)map.get("userId");
				Integer roleId = (Integer)map.get("roleId");
				user.setUserId(userId);
				user.setRoleId(roleId);				
				session.setAttribute("user", user);				
				return 0;    //验证成功				
			}
		}
	}
}
