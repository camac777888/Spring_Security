package com.sn.Security.constants;

public final class SecurityConstants {
    //過期時間=1小時
    public static final  long EXPIRATION = 3600L;
    //過期時間=7天
    public static final  long EXPIRATION_REMEMBER = 604800L;;
    public static final  String TOKEN_HEADER="Authorization";
    public static final  String TOKEN_PREFIX="Bearer ";

    public static final  String SUBJECT = "winter";
    public static final  String SIGN_SECRET = "bastard";
    public static final String TOKEN_ROLE_CLAIM = "role";
}
