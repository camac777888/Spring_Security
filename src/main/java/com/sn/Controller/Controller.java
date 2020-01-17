package com.sn.Controller;



import com.sn.Domain.Auth;
import com.sn.Domain.User;
import com.sn.Mapper.UserMapper;
import com.sn.Service.MyUserDetailService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@org.springframework.stereotype.Controller
@RequestMapping(value = "/api",produces = "application/json;charset=utf-8")
public class Controller {

    @Autowired
    private MyUserDetailService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

@GetMapping("/login")
public String login(){
        return"login";
}

    @GetMapping("/index")
    public String index(){
        return"index";
    }


    @GetMapping("/healthcheck")
    @ResponseBody
    public String healthcheck(){return "success";}


//    /**
//     * 注冊
//     * @param user
//     * @return
//     */
    @PostMapping("/register")
    @ResponseBody
    public String register(@RequestBody User user){
        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword()) || StringUtils.isBlank(user.getName()) ){
            return "參數不得為空值";
        }
        if (userService.findByUsername(user.getUsername())!=null){
            return "帳號已存在";
        }
        String DATE = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String  encodePassword  = passwordEncoder.encode(user.getPassword().trim());
        user.setPassword(encodePassword);
        user.setCreateDate(DATE);
        user.setLastLoginDate(DATE);
        userService.createAccount(user);
        return "success";
    }

    /**
     * 修改密碼
     * @param user
     * @return
     */
    @PostMapping("/modify")
    @ResponseBody
public String modify(@RequestBody User user){
        String  encodePassword  = passwordEncoder.encode(user.getPassword().trim());
        user.setPassword(encodePassword);
        userService.updatePassword(user);
    return "success";
}

    /**
     * 查看當前token的權限
     * @param authentication
     * @return
     */
    @GetMapping("/checkAuth")
    @ResponseBody
    public String checkAuth(Authentication authentication){

    return authentication.getAuthorities().toString();}

    }



