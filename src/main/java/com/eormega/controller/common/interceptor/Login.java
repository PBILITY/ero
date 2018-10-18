package com.eormega.controller.common.interceptor;

import com.github.pagehelper.StringUtil;
import com.eormega.common.Const;
import com.eormega.common.ServerResponse;
import com.eormega.util.CookieUtil;
import com.eormega.util.JsonUtil;
import com.eormega.util.RedisShardedPoolUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class Login implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        try{
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter out = null ;
            String loginToken = CookieUtil.readLoginToken(request);
            if (StringUtil.isEmpty(loginToken)){
                    out = response.getWriter();
                    out.append(JsonUtil.obj2String(ServerResponse.createByErrorMessage("用户未登录")));
                    return false;
            } else {
                RedisShardedPoolUtil.expire(loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
                return true;
            }
        } catch(Exception e){
            e.printStackTrace();
            response.sendError(500);
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
