package com.gf.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.gf.utils.JsonResult;
import com.gf.utils.JwtTokenUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Qualifier("userDetailsServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 存放Token的Header Key
     */
    public static final String HEADER_STRING = "Authorization";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        String token = request.getHeader(HEADER_STRING);
        if (StringUtils.equals("/auth/login", request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }
        if (StringUtils.equals("/user/addUser", request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }
        /*if (StringUtils.equals("/findPager", request.getRequestURI())){
            chain.doFilter(request, response);
            return;
        }*/
        if (null != token) {
            String username = jwtTokenUtil.getUsernameFromToken(token);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                if (jwtTokenUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(
                            request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonResult jsonResult = new JsonResult();
            jsonResult.setRtCode(401);
            jsonResult.setRtMsg("未检测到有效登录信息");
            //response.sendRedirect(loginUrl);
            PrintWriter out = response.getWriter();
            out.write(objectMapper.writeValueAsString(jsonResult));
            out.flush();
            out.close();
        }
        chain.doFilter(request, response);
    }

}
