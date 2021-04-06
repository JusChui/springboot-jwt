package com.gf.handler;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author JusChui
 * @ClassName MyAuthenticationFailureHandler.java
 * @Date 2021年03月28日 23:52:00
 * @Description TODO
 */
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private String url;

    //public MyAuthenticationFailureHandler(String url) {
    //    this.url = url;
    //}

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        String requestURI = httpServletRequest.getRequestURI();
        System.out.println(requestURI);
        httpServletResponse.sendRedirect(requestURI);
    }
}
