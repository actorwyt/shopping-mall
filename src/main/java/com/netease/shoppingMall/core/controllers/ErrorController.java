package com.netease.shoppingMall.core.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/error")
public class ErrorController {
    @RequestMapping(value = "/{errorCode}")
    public ModelAndView error(@PathVariable int errorCode) {
        switch (errorCode) {
            case 404:
                return new ModelAndView("errors/error");  
            default:
                return new ModelAndView("errors/error");
        }
    }
}
