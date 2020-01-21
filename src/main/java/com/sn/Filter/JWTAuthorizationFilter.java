package com.sn.Filter;

import com.sn.Security.Util.JWTUtil;
import com.sn.Security.constants.SecurityConstants;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {


    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String token = request.getHeader(SecurityConstants.TOKEN_HEADER);
        // 判断是否有token,如果请求头中没有Authorization信息则直接放行了
        if (token == null || !token.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(token);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        //放行
        chain.doFilter(request, response);


    }


    /**
     * 解析token中的信息,并判断是否过期
     */
    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        //清除前綴
        String token1 = token.replace(SecurityConstants.TOKEN_PREFIX, "");
        JWTUtil jwtUtil = new JWTUtil();

        //得到用户名
        String username = jwtUtil.getUsername(token1);

        List<GrantedAuthority> authorities = jwtUtil.getRoles(token1);
        //得到过期时间
        Date expiration =  jwtUtil.getExpiration(token1);

        //判断是否过期
        Date now = new Date();

        if (now.getTime() > expiration.getTime()) {

            throw new RuntimeException("该账号已过期,请重新登陆");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
        authenticationToken.setDetails("有效日期: "+expiration);
        if (username != null) {
            return authenticationToken;
        }
        return null;
    }





}