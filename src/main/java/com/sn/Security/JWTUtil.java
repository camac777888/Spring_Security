package com.sn.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class JWTUtil {

    public static  String TOKEN_HEADER="Authorization";
    public static  String TOKEN_PREFIX="Bearer ";

    private static final String SUBJECT = "winter";
    private static final String SIGN_SECRET = "bastard";
    private static final String TOKEN_ROLE_CLAIM = "role";
    //過期時間=1小時
    private static final long EXPIRATION = 3600L;
    //過期時間=7天
    private static final long EXPIRATION_REMEMBER = 604800L;;

    /**
     * 產生token
     * @param username
     * @param isRememberMe
     * @return
     */
    public String createToken(String username, boolean isRememberMe, List<String> roles){
        long expiration  =isRememberMe ?EXPIRATION_REMEMBER:EXPIRATION;

      return Jwts.builder()
              .setSubject(SUBJECT)//主题
              .setIssuer(username) //签发人
              .claim(TOKEN_ROLE_CLAIM, roles)
              .setIssuedAt(new Date()) //用於設定簽發時間
              .signWith(SignatureAlgorithm.HS512,SIGN_SECRET) //设置算法（必须）
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
                .setSigningKey(SIGN_SECRET)
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
        List<?> roles = (List<?>)getTokenBody(token).get(TOKEN_ROLE_CLAIM);
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority((String) role))
                .collect(Collectors.toList());


    }




}
