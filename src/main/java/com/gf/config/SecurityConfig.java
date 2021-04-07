package com.gf.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.gf.filter.JwtTokenFilter;
import com.gf.handler.MyAccessDeniedHandler;
import com.gf.handler.MyAuthenticationFailureHandler;
import com.gf.handler.MyAuthenticationSuccessHandler;
import com.gf.handler.MyLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.DigestUtils;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //登录失败处理
    private final MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    //退出成功处理
    private final MyLogoutSuccessHandler myLogoutSuccessHandler;

    private final MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    private final UserDetailsService userDetailsService;

    public SecurityConfig(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService, MyAuthenticationFailureHandler myAuthenticationFailureHandler, MyLogoutSuccessHandler myLogoutSuccessHandler, MyAuthenticationSuccessHandler myAuthenticationSuccessHandler) {
        this.userDetailsService = userDetailsService;
        this.myAuthenticationFailureHandler = myAuthenticationFailureHandler;
        this.myLogoutSuccessHandler = myLogoutSuccessHandler;
        this.myAuthenticationSuccessHandler = myAuthenticationSuccessHandler;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        //校验用户
        auth.userDetailsService(userDetailsService).passwordEncoder(new PasswordEncoder() {
            //对密码进行加密
            @Override
            public String encode(CharSequence charSequence) {
                //System.out.println(charSequence.toString());
                //System.out.println("123加密===>" + DigestUtils.md5DigestAsHex("123".getBytes()));
                return DigestUtils.md5DigestAsHex(charSequence.toString().getBytes());
            }

            //对密码进行判断匹配
            @Override
            public boolean matches(CharSequence charSequence, String s) {
                String encode = DigestUtils.md5DigestAsHex(charSequence.toString().getBytes());
                boolean res = s.equals(encode);
                /*if (res == false){
                    throw  new RuntimeException("密码有误，请重新输入");
                }*/
                return res;
            }
        });

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.formLogin()
                .loginPage("http://localhost:8080/login")
                .successHandler(myAuthenticationSuccessHandler)
                .failureHandler(myAuthenticationFailureHandler)
                .and()
                .logout()
                //退出成功，返回json
                .logoutSuccessHandler(myLogoutSuccessHandler);

        http.csrf().disable()
                //因为使用JWT，所以不需要HttpSession
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                //登录接口放行
                .antMatchers("/auth/login").permitAll()
                .antMatchers("/user/addUser").permitAll()
                //其他接口全部接受验证
                .anyRequest().authenticated();

        http.exceptionHandling().accessDeniedHandler(new MyAccessDeniedHandler());

        //使用自定义的 Token过滤器 验证请求的Token是否合法
        http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
        http.headers().cacheControl();
    }

    @Bean
    public JwtTokenFilter authenticationTokenFilterBean() throws Exception {
        return new JwtTokenFilter();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


}
