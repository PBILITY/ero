package com.eormega.controller.common.interceptor;

import com.eormega.common.Const;
import com.eormega.common.ServerResponse;
import com.eormega.pojo.User;
import com.eormega.util.CookieUtil;
import com.eormega.util.JsonUtil;
import com.eormega.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public class AuthorityInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        log.info("preHandle");
        HandlerMethod handlerMethod = (HandlerMethod)o;

        //解析HandlerMethod

        String methodName = handlerMethod.getMethod().getName();
        String className = handlerMethod.getBean().getClass().getSimpleName();

        //解析参数，具体的参数key以及value是什么，
        StringBuffer requestParamBuffer = new StringBuffer();
        Map paramMap = httpServletRequest.getParameterMap();
        Iterator it= paramMap.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry entry = (Map.Entry)it.next();
            String mapKey = (String)entry.getKey();
            String mapValue = StringUtils.EMPTY;
            //request这个参数的map.里面的value返回的是一个String[]
            Object obj = entry.getValue();
            if(obj instanceof  String[]){
                String[] strs = (String[])obj;
                mapValue = Arrays.toString(strs);
            }
            requestParamBuffer.append(mapKey).append("=").append(mapValue);
        }
        User user = null;

        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isNotEmpty(loginToken)){
            String userJsonStr = RedisShardedPoolUtil.get(loginToken);
            user = JsonUtil.string2Obj(userJsonStr, User.class);
        }
        if(user == null || (user.getRole().intValue() != Const.Role.ROLE_ADMIN)){
            httpServletResponse.reset();
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json;charset=UTF-8");

            PrintWriter out = httpServletResponse.getWriter();
            if (user == null){
                out.print(JsonUtil.obj2String(ServerResponse.createByErrorMessage("用户为登陆")));
            } else {
                out.print(JsonUtil.obj2String(ServerResponse.createByErrorMessage("无权限")));
            }
            out.flush();
            out.close();
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        log.info("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        log.info("afterCompletion");
    }
}
