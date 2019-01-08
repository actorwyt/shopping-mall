package com.netease.shoppingMall.core.service.face;

import javax.servlet.http.HttpSession;

import com.netease.shoppingMall.core.domain.User;

public interface LoginInterface {
	public int doLogin(User loginInfo, HttpSession session);
}
