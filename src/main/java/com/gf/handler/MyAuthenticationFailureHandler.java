package com.gf.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gf.utils.JsonResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author JusChui
 * @ClassName MyAuthenticationFailureHandler.java
 * @Date 2021年03月28日 23:52:00
 * @Description TODO
 */
@Component
public class MyAuthenticationFailureHandler  extends SimpleUrlAuthenticationFailureHandler {

    private String url;

    //public MyAuthenticationFailureHandler(String url) {
    //    this.url = url;
    //}

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        JsonResult jsonResult = new JsonResult();
        jsonResult.setRtCode(401);
        jsonResult.setRtMsg("Login Failure");
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(jsonResult);
        httpServletResponse.setContentType("application/json;charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();
        out.write(s);
        out.flush();
    }
}
