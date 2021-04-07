package com.gf.controller;

import com.gf.service.IUserService;
import com.gf.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author JusChui
 * @ClassName UserController.java
 * @Date 2021年04月07日 20:47:00
 * @Description TODO
 */
@RestController
public class UserController {

    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @RequestMapping(path = "/user/addUser", method = {RequestMethod.POST})
    @ResponseBody
    public JsonResult addUser(@RequestBody Map<String, Object> params) {
        JsonResult jsonResult;
        jsonResult = userService.addUser(params);
        return jsonResult;
    }
}
