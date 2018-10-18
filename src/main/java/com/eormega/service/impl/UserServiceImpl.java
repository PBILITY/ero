package com.eormega.service.impl;

import com.eormega.common.Const;
import com.eormega.common.ServerResponse;
import com.eormega.dao.UserMapper;
import com.eormega.pojo.User;
import com.eormega.service.IUserService;
import com.eormega.util.MD5Util;
import com.eormega.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by okomo
 */
@Slf4j
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public ServerResponse<User> login(String username, String password) {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        token.setRememberMe(true);
        //获取当前的Subject
        Subject currentUser = SecurityUtils.getSubject();
        try {
            //在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
            //每个Realm都能在必要时对提交的AuthenticationTokens作出反应
            //所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
            log.error("对用户[" + username + "]进行登录验证..验证开始");
            currentUser.login(token);
            log.error("对用户[" + username + "]进行登录验证..验证通过");
        }catch(UnknownAccountException uae){
            log.error("对用户[" + username + "]进行登录验证..验证未通过,未知账户");
            return ServerResponse.createByErrorMessage("未知账户");
        }catch(IncorrectCredentialsException ice){
            log.error("对用户[" + username + "]进行登录验证..验证未通过,密码错误");
            return ServerResponse.createByErrorMessage("密码错误");
        }catch(LockedAccountException lae){
            log.error("对用户[" + username + "]进行登录验证..验证未通过,账户已锁定");
            return ServerResponse.createByErrorMessage("账户已锁定");
        }catch(ExcessiveAttemptsException eae){
            log.error("对用户[" + username + "]进行登录验证..验证未通过,错误次数过多");
            return ServerResponse.createByErrorMessage("错误次数过多");
        }catch(AuthenticationException ae){
            //通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
            log.error("对用户[" + username + "]进行登录验证..验证未通过,堆栈轨迹如下");
            ae.printStackTrace();
        }
        //验证是否登录成功
        if(currentUser.isAuthenticated()){
            log.error("用户[" + username + "]登录认证通过(这里可以进行一些认证通过后的一些系统参数初始化操作)");
            User user = (User)currentUser.getSession().getAttribute("currentUser");
            return ServerResponse.createBySuccess("登录成功",user);
        }else{
            token.clear();
            return ServerResponse.createByErrorMessage("登录失败");
        }
    }



    public ServerResponse<String> register(User user){
        ServerResponse validResponse = this.checkValid(user.getUsername(),Const.USERNAME);
        if(!validResponse.isSuccess()){
            return validResponse;
        }
        user.setRole(Const.Role.ROLE_CUSTOMER);
        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resultCount = userMapper.insert(user);
        if(resultCount == 0){
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    public ServerResponse<String> checkValid(String str,String type){
        if(org.apache.commons.lang3.StringUtils.isNotBlank(type)){
            //开始校验
            if(Const.USERNAME.equals(type)){
                int resultCount = userMapper.checkUsername(str);
                if(resultCount > 0 ){
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }
            if(Const.EMAIL.equals(type)){
                int resultCount = userMapper.checkEmail(str);
                if(resultCount > 0 ){
                    return ServerResponse.createByErrorMessage("email已存在");
                }
            }
        }else{
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }

    public ServerResponse selectQuestion(String username){

        ServerResponse validResponse = this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess()){
            //用户不存在
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if(org.apache.commons.lang3.StringUtils.isNotBlank(question)){
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("找回密码的问题是空的");
    }

    public ServerResponse<String> checkAnswer(String username,String question,String answer){
        int resultCount = userMapper.checkAnswer(username,question,answer);
        if(resultCount>0){
            //说明问题及问题答案是这个用户的,并且是正确的
            String forgetToken = UUID.randomUUID().toString();
            RedisShardedPoolUtil.setEx(Const.TOKEN_PREFIX+username,forgetToken,60*60*12);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题的答案错误");
    }



    public ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken){
        if(org.apache.commons.lang3.StringUtils.isBlank(forgetToken)){
            return ServerResponse.createByErrorMessage("参数错误,token需要传递");
        }
        ServerResponse validResponse = this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess()){
            //用户不存在
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String token = RedisShardedPoolUtil.get(Const.TOKEN_PREFIX+username);
        if(org.apache.commons.lang3.StringUtils.isBlank(token)){
            return ServerResponse.createByErrorMessage("token无效或者过期");
        }

        if(org.apache.commons.lang3.StringUtils.equals(forgetToken,token)){
            String md5Password  = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.updatePasswordByUsername(username,md5Password);

            if(rowCount > 0){
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }
        }else{
            return ServerResponse.createByErrorMessage("token错误,请重新获取重置密码的token");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }


    public ServerResponse<String> resetPassword(String passwordOld,String passwordNew,User user){
        //防止横向越权,要校验一下这个用户的旧密码,一定要指定是这个用户.因为我们会查询一个count(1),如果不指定id,那么结果就是true啦count>0;
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),user.getId());
        if(resultCount == 0){
            return ServerResponse.createByErrorMessage("旧密码错误");
        }

        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if(updateCount > 0){
            return ServerResponse.createBySuccessMessage("密码更新成功");
        }
        return ServerResponse.createByErrorMessage("密码更新失败");
    }


    public ServerResponse<User> updateInformation(User user){
        //username是不能被更新的
        //email也要进行一个校验,校验新的email是不是已经存在,并且存在的email如果相同的话,不能是我们当前的这个用户的.
//        int resultCount = userMapper.checkEmailByUserId(user.getEmail(),user.getId());
//        if(resultCount > 0){
//            return ServerResponse.createByErrorMessage("email已存在,请更换email再尝试更新");
//        }
        User updateUser = new User();
        updateUser.setId(user.getId());
//        updateUser.setEmail(user.getEmail());
//        updateUser.setPhone(user.getPhone());
//        updateUser.setQuestion(user.getQuestion());
//        updateUser.setAnswer(user.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if(updateCount > 0){
            return ServerResponse.createBySuccess("更新个人信息成功",updateUser);
        }
        return ServerResponse.createByErrorMessage("更新个人信息失败");
    }



    public ServerResponse<User> getInformation(Integer userId){
        User user = userMapper.selectByPrimaryKey(userId);
        if(user == null){
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }
        user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);

    }




    //backend

    /**
     * 校验是否是管理员
     * @param user
     * @return
     */
    public ServerResponse checkAdminRole(User user){
        if(user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

    public User getByAccount(String username){
        return userMapper.getByUsername(username);
    }

}
