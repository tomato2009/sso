package com.bochtec.ssoclient.intercept;

import com.bochtec.ssoclient.util.HttpUtil;
import com.bochtec.ssoclient.util.SSOClientUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

public class SessionIntercept implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession();

        //1.判断是否有局部的会话
        Boolean isLogin = (Boolean) session.getAttribute("isLogin");
        if(isLogin!=null && isLogin){
            //有局部会话,直接放行.
            return true;
        }

        //判断地址栏中是否有携带token参数.
        String token = req.getParameter("token");
        if(StringUtils.hasLength(token)){
            //token信息不为null,说明地址中包含了token,拥有令牌.
            //判断token信息是否由认证中心产生的.
            //验证地址为:http://www.sso.com:8443/verify
            String httpURL = SSOClientUtil.SERVER_URL_PREFIX+"/verify";
            Map<String,String> params = new HashMap<String,String>();
            //把客户端地址栏添加到的token信息传递给统一认证中心进行校验
            params.put("token", token);
            /**-------------------------阶段四添加的代码start---------------------------------**/
            //获取客户端的完整登出地址 http://www.crm.com:8088/logOut
            params.put("clientUrl", SSOClientUtil.getClientLogOutUrl());
            params.put("jsessionid", session.getId());
            /**-------------------------阶段四添加的代码end---------------------------------**/
            try {
                String isVerify = HttpUtil.sendHttpRequest(httpURL, params);
                if("true".equals(isVerify)){
                    //如果返回的字符串是true,说明这个token是由统一认证中心产生的.
                    //创建局部的会话.
                    session.setAttribute("isLogin", true);
                    //放行该次的请求
                    return true;
                }
            } catch (Exception e) {
                //这里可以完善,比如出现异常在前台显示具体页面
                //我们这个案例就不做这个哈.
                e.printStackTrace();
            }
        }
        //没有局部会话,重定向到统一认证中心,检查是否有其他的系统已经登录过.
        // http://www.sso.com:8443/checkLogin?redirectUrl=http://www.crm.com:8088
        SSOClientUtil.redirectToSSOURL(req, resp);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
