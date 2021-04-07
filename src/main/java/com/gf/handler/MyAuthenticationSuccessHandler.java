package com.gf.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gf.utils.JsonResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author JusChui
 * @ClassName MyAuthenticationSuccessHandler.java
 * @Date 2021年03月28日 23:42:00
 * @Description TODO
 */
@Component
public class MyAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private String url;

    /*public MyAuthenticationSuccessHandler(String s) {
        this.url = s;
    }*/

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();
        String requestURI = httpServletRequest.getRequestURI();
        this.url = requestURI;
        httpServletResponse.sendRedirect(url);

        JsonResult jsonResult = new JsonResult();
        jsonResult.setRtCode(200);
        jsonResult.setRtMsg("Login Success");
        jsonResult.setData(user);
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(jsonResult);
        httpServletResponse.setContentType("application/json;charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();
        out.write(s);
        out.flush();
    }
}
