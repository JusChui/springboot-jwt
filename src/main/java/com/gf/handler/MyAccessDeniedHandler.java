package com.gf.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gf.utils.JsonResult;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author JusChui
 * @ClassName MyAccessDeniedHandler.java
 * @Date 2021年03月29日 00:37:00
 * @Description TODO
 */
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                       AccessDeniedException e) throws IOException, ServletException {
        JsonResult jsonResult = new JsonResult();
        ObjectMapper mapper = new ObjectMapper();
        jsonResult.setRtCode(HttpServletResponse.SC_FORBIDDEN);
        jsonResult.setRtMsg(e.getMessage());

        //响应状态
        //System.out.println(e.getMessage());
        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        httpServletResponse.setHeader("Content-type", "application/json;charset=utf-8");
        PrintWriter writer = httpServletResponse.getWriter();
        writer.write(mapper.writeValueAsString(jsonResult));
        writer.flush();
        writer.close();
    }
}
