package com.sn.Security.Util;
import  com.sn.Security.constants.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public final class JWTUtil {


    /**
     * 產生token
     * @param username
     * @param isRememberMe
     * @return
     */
    public String createToken(String username, boolean isRememberMe, List<String> roles){
        long expiration  =isRememberMe ?SecurityConstants.EXPIRATION_REMEMBER:SecurityConstants.EXPIRATION;

      return Jwts.builder()
              .setSubject(SecurityConstants.SUBJECT)//主题
              .setIssuer(username) //签发人
              .claim(SecurityConstants.TOKEN_ROLE_CLAIM, roles)
              .setIssuedAt(new Date()) //用於設定簽發時間
              .signWith(SignatureAlgorithm.HS512,SecurityConstants.SIGN_SECRET) //设置算法（必须）
              .setExpiration(new Date(System.currentTimeMillis()+expiration*1000))//設定token的過期時間(毫秒)
              .compact();//这个是全部设置完成后拼成jwt串的方法
    }

    /**
     * 解析token
     * @param token
     * @return
     */
    private Claims getTokenBody(String token){
        return Jwts.parser()
                .setSigningKey(SecurityConstants.SIGN_SECRET)
                .parseClaimsJws(token)
                .getBody(); //得到body后我们可以从body中获取我们需要的信息

    }
    //token中獲取用戶名
    public String getUsername(String token){
        return getTokenBody(token).getIssuer();
    }
    public Date getExpiration(String token){
        return getTokenBody(token).getExpiration();
    }


    public  List<GrantedAuthority> getRoles(String token) {
        List<?> roles = (List<?>)getTokenBody(token).get(SecurityConstants.TOKEN_ROLE_CLAIM);
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority((String) role))
                .collect(Collectors.toList());


    }




}
