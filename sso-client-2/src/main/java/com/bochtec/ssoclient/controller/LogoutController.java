package com.bochtec.ssoclient.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class LogoutController {
    @RequestMapping("logOut")
    public void logout(HttpSession session) {
        session.invalidate();
    }
}
