package com.bochtec.ssoclient.controller;

import com.bochtec.ssoclient.util.SSOClientUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

    @RequestMapping("main")
    public ModelAndView main() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("serverLogOutUrl", SSOClientUtil.getServerLogOutUrl());

        modelAndView.setViewName("main");

        return modelAndView;
    }
}
