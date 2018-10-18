package com.eormega.controller.backend;

import com.eormega.common.Const;
import com.eormega.common.ServerResponse;
import com.eormega.pojo.User;
import com.eormega.service.IUserService;
import com.eormega.util.CookieUtil;
import com.eormega.util.JsonUtil;
import com.eormega.util.RedisShardedPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by okomo
 */

@Controller
@RequestMapping("/manage/user")
public class UserManageController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value="login.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest){
        ServerResponse<User> response = iUserService.login(username,password);
        if(response.isSuccess()){
            User user = response.getData();
            if(user.getRole() == Const.Role.ROLE_ADMIN){
                //说明登录的是管理员
                //session.setAttribute(Const.CURRENT_USER,user);
                CookieUtil.writeLoginToken(httpServletResponse,session.getId());
                String loginToken = CookieUtil.readLoginToken(httpServletRequest);
                RedisShardedPoolUtil.setEx(loginToken, JsonUtil.obj2String(response.getData()),Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
                return response;
            }else{
                return ServerResponse.createByErrorMessage("不是管理员,无法登录");
            }
        }
        return response;
    }

}
