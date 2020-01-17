package com.sn.Provider;

import com.sn.Domain.Auth;
import com.sn.Domain.User;
import com.sn.Service.MyUserDetailService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component("CustomAuthenticationProvider")
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private MyUserDetailService myUserDetailService;
    @Autowired
    private  PasswordEncoder passwordEncoder;


    /**
     *
     * 验证登录信息,若登陆成功,设置 Authentication
     * @param authentication
     * @return 一个完全经过身份验证的对象，包括凭证
     * 如果AuthenticationProvider无法支持已通过的身份验证对象的身份验证，则可能返回null。
     * 在这种情况下，将会尝试支持下一个身份验证类的验证提供者。
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取认证信息的用户名和密码
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        if(StringUtils.isBlank(username)){
            throw new UsernameNotFoundException("username用户名不可以为空");
        }
        if(StringUtils.isBlank(password)){
            throw new BadCredentialsException("密码不可以为空");
        }

//通过用户名从数据库中查询该用户
        UserDetails userDetails  = myUserDetailService.loadUserByUsername(username);
    if(userDetails==null){
        throw new UsernameNotFoundException("查無此用戶名");
    }
    if(!userDetails.isAccountNonExpired()){
        throw new AccountExpiredException("用戶過期");
    }
        if(!userDetails.isAccountNonLocked()){
            throw new LockedException("用戶為鎖定狀態");
        }
        if(!userDetails.isEnabled()){
            throw new DisabledException("用戶為不可用狀態");
        }
        if(!userDetails.isCredentialsNonExpired()){
            throw new CredentialsExpiredException("用戶憑證過期");
        }
        if(passwordEncoder.matches(password,userDetails.getPassword())){

            return new UsernamePasswordAuthenticationToken(username, password,userDetails.getAuthorities());

        }else{
            throw new BadCredentialsException("The wrong password.");
        }



    }

    /**
     * 是否可以提供输入类型的认证服务
     * 如果这个AuthenticationProvider支持指定的身份验证对象，那么返回true。
     * 返回true并不能保证身份验证提供者能够对身份验证类的实例进行身份验证。
     * 它只是表明它可以支持对它进行更深入的评估。身份验证提供者仍然可以从身份验证(身份验证)方法返回null，
     * 以表明应该尝试另一个身份验证提供者。在运行时管理器的运行时，可以选择具有执行身份验证的身份验证提供者。
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
//        return true;
    }
}
