package com.eormega.controller.portal;


import com.eormega.common.ServerResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/task/")
public class TaskContorller {

    @RequestMapping(value = "start.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> start(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){

        return ServerResponse.createBySuccess();
    }
}
