package com.sn.Service;

import com.sn.Domain.Auth;
import com.sn.Domain.User;
import com.sn.Mapper.UserMapper;
;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("MyUserDetailService")
public class MyUserDetailService extends BaseService implements UserDetailsService {

//    private Logger logger = Logger.getLogger(MyUserDetailService.class);

    @Resource
    private UserMapper userMapper;

//    public void createAccount (User user){
//    }

public User findByUsername(String username){
        return userMapper.findByUsername(username);
}

public  List<Auth>  findAuthByUsername(String username){
    return userMapper.findAuthByUsername(username);
}

    public void updatePassword(User user){
      userMapper.updatePassword(user);
    };
    public void createAccount(User user){
       userMapper.findByUsername(user.getUsername());
    };



    /**
     * loadUserByUsername:提供一种从用户名可以查到用户并返回的方法
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       //BasicConfigurator.configure()使用默认的配置信息，不需要写log4j.properties
//        BasicConfigurator.configure();

        User user = userMapper.findByUsername(username);
        log.info("藉由輸入的帳號:{},從資料庫中抓取用戶資料: {}",username,user);
        if(user!=null){
        //根據用戶名查詢當前用戶所有權限
        List<Auth> authList =  userMapper.findAuthByUsername(username);
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        for (Auth au :authList){
            GrantedAuthority authority = new SimpleGrantedAuthority(au.getAuthInfo());
            authorities.add(authority);
        }
        log.info("抓取資料庫存取權限:{}",authorities);

        user.setAuthorities(authorities);
//        logger.info("當前用戶:"+user);
        }
        return user;
    }
}
