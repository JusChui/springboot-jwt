package com.gf.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author JusChui
 * @ClassName MyAuthenticationSuccessHandler.java
 * @Date 2021年03月28日 23:42:00
 * @Description TODO
 */
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private String url;

    /*public MyAuthenticationSuccessHandler(String s) {
        this.url = s;
    }*/

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();
        String requestURI = httpServletRequest.getRequestURI();
        System.out.println(requestURI);
        this.url = requestURI;
        /*System.out.println(httpServletRequest.getRequestURI());
        System.out.println("--------------");
        System.out.println(httpServletRequest.toString());*/
        /*System.out.println(user.getUsername());
        //出于安全考虑，password会直接输出null
        System.out.println(user.getPassword());
        System.out.println(user.getAuthorities());
        System.out.println("---------------");
        System.out.println(user.toString());*/
        httpServletResponse.sendRedirect(url);
    }
}
