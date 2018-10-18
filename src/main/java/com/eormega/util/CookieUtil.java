package com.eormega.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class CookieUtil {
    private final static String COOKIE_DOMAIN = ".zhou.com";
    private final static String COOKIE_NAME = "mmall_login_token";

    public static String readLoginToken(HttpServletRequest request){
        Cookie[] cks = request.getCookies();
        if(cks != null){
            for (Cookie ck:
                 cks) {
                if(StringUtils.equals(ck.getName(),COOKIE_NAME)){
                    return ck.getValue();
                }
            }
        }
        return null;
    }

    public static void writeLoginToken(HttpServletResponse response, String token){
        Cookie ck = new Cookie(COOKIE_NAME,token);
        ck.setDomain(COOKIE_DOMAIN );
        ck.setPath("/");//代表设置在根目录
        ck.setHttpOnly(true);//防止脚本
        //单位是秒
        //如果maxage不设置的化，cookie就不会写入硬盘，而是写在内存，只有当前页面有效
        ck.setMaxAge(60 * 60 * 24 * 365);//如果是-1，代表永久
        response.addCookie(ck);
    }

    public static void delLoginToken(HttpServletResponse response,HttpServletRequest request){
        Cookie[] cks = request.getCookies();
        if(cks != null){
            for (Cookie ck :
                    cks) {
                if (StringUtils.equals(ck.getName(), COOKIE_NAME)) {
                    ck.setDomain(COOKIE_DOMAIN);
                    ck.setPath("/");
                    ck.setMaxAge(0);//0删除次COOKIE
                    response.addCookie(ck);
                    return;
                }
                }
        }
    }
}
