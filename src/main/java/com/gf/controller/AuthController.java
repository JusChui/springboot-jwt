package com.gf.controller;

import com.gf.entity.User;
import com.gf.service.AuthService;
import com.gf.utils.JsonResult;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @Qualifier("userDetailsServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 登录
     */
    @PostMapping(value = "/auth/login")
    @ResponseBody
    public JsonResult login(@RequestBody Map<String, Object> params) throws AuthenticationException {
        String username = (String) params.get("username");
        String password = (String) params.get("password");
        JsonResult result = new JsonResult();
        String token = authService.login(username, password);
        Map<String, Object> bean = new HashMap();
        bean.put("token", token);
        result.setBean(bean);
        User user = (User) userDetailsService.loadUserByUsername(username);
        if (null != token) {
            result.setData(user);
            result.setRtCode(200);
            result.setRtMsg("Login Success");
        }
        return result;
    }

    @PostMapping(value = "/user/hi")
    public String userHi(String name) throws AuthenticationException {
        return "hi " + name + " , you have 'user' role";
    }

    @PostMapping(value = "/admin/hi")
    public String adminHi(String name) throws AuthenticationException {
        return "hi " + name + " , you have 'admin' role";
    }


}
