package com.sn.Security;

import com.sn.Filter.JWTAuthorizationFilter;
import com.sn.Filter.JWTLoginFilter;
import com.sn.Provider.CustomAuthenticationProvider;
import com.sn.Service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
@EnableWebSecurity(debug = true)    //debug 開啟後可看到filterchain的所有filter
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationProvider authProvider;
    @Autowired
    private  MyUserDetailService usersService;


    /**
     * 添加用户名和密码登陆验证的过滤器
     */
    @Bean
    public JWTLoginFilter jWTLoginFilter() throws Exception {
        JWTLoginFilter filter = new JWTLoginFilter();
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }








    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.headers().cacheControl();     // 禁用缓存
            http.authorizeRequests()
                    .antMatchers("/api/**").permitAll() // 调用api不需要拦截
                    .antMatchers("/sec/**").authenticated() //都需要身分認證
                    .and()

                    .addFilter(jWTLoginFilter())

//                    .addFilterBefore(jWTLoginFilter(), UsernamePasswordAuthenticationFilter.class)
                    //验证token
                    .addFilter(new JWTAuthorizationFilter(authenticationManager()))
                    .csrf().disable()// 取消csrf
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)// 基于token，所以不需要session
                   ;;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth
               .authenticationProvider(authProvider).eraseCredentials(true);
    }

    //在这里配置哪些页面不需要认证
    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(HttpMethod.GET,
                        "/*.html",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js"
                        );


//        web.ignoring().antMatchers("/register");

    }

    /**
     *  自定义加密
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    }






