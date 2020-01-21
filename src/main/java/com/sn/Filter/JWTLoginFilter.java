package com.sn.Filter;


import com.fasterxml.jackson.databind.ObjectMapper;

import com.sn.Security.Util.JWTUtil;
import com.sn.Security.constants.SecurityConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter  {

    protected transient final Logger log = LoggerFactory.getLogger(getClass());

    private ObjectMapper objectMapper = new ObjectMapper();
    public JWTLoginFilter() {
        //父类中定义了拦截的请求URL，/login的post请求，直接使用这个配置，也可以自己重写
        super.setFilterProcessesUrl("/permit/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
//得到用户登陆信息,并封装到 Authentication 中,供自定义用户组件使用.
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (username == null) {
            username = "";
        }

        if (password == null) {
            password = "";
        }
        username = username.trim();

        UsernamePasswordAuthenticationToken authRequest =  new UsernamePasswordAuthenticationToken(username, password,new ArrayList<>());
        setDetails(request, authRequest);
        Authentication authentication = getAuthenticationManager().authenticate(authRequest);
        return authentication;

    }

    // 成功验证后调用的方法
    // 如果验证成功，就生成token并返回
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException{

      boolean isRememberMe = false;
            if("on".equals(request.getParameter("remember_me"))){
                isRememberMe = true ;
            }
               // 获取用户认证权限
        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        // 获取用户角色
        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    if(roles.size()==0){
        roles.add("ROLE_USER");
        log.info("該用戶未設定權限，默認配予{}",roles);
    }
        JWTUtil jwtUtil =new JWTUtil();
        String token = jwtUtil.createToken(authResult.getName(), isRememberMe,roles);
        // 返回创建成功的token
        // 但是这里创建的token只是单纯的token
        // 按照jwt的规定，最后请求的时候应该是 `Bearer token`

        response.setHeader("token", SecurityConstants.TOKEN_PREFIX + token);
        response.setContentType("application/json;charset=utf-8");

        Map result = new HashMap();

        result.put("code",200);
        result.put("msg","success");
        result.put("token",SecurityConstants.TOKEN_PREFIX + token);
        String json =objectMapper.writeValueAsString(result);
        response.getWriter().write(json);
        response.getWriter().flush();
        response.getWriter().close();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        Map result = new HashMap();
        result.put("code",500);
        if (e instanceof UsernameNotFoundException || e instanceof BadCredentialsException) {
            result.put("msg",e.getMessage());
        } else {
            result.put("msg","登录失败!,原因: "+e.getMessage());
        }

        String json =objectMapper.writeValueAsString(result);
        response.getWriter().write(json);
        response.getWriter().flush();
        response.getWriter().close();
    }
}

