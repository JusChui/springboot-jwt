package com.gf.controller;

import com.gf.utils.PageRequest;
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

    @RequestMapping(path = "/user/getStudents", method = {RequestMethod.POST})
    @ResponseBody
    public JsonResult getAllStudent(@RequestBody Map<String, Object> params, PageRequest pageQuery) {
        JsonResult jsonResult;
        Integer pageSize = (Integer) params.get("pageSize");    //每页数量
        Integer pageNum = (Integer) params.get("currentPage");  //当前页码
        pageQuery.setPageNum(pageNum);
        pageQuery.setPageSize(pageSize);
        jsonResult = userService.getStudents(params, pageQuery);
        //jsonResult = userService.getStudents(params);
        return jsonResult;
    }

    /*//测试分页
    @PostMapping(value = "/findPage")
    @ResponseBody
    public JsonResult findPage(@RequestBody Map<String, Object> params, PageRequest pageQuery) {
        JsonResult jsonResult = new JsonResult();
        Integer pageSize = (Integer) params.get("pageSize");    //每页数量
        Integer pageNum = (Integer) params.get("currentPage");  //当前页码
        pageQuery.setPageNum(pageNum);
        pageQuery.setPageSize(pageSize);
        jsonResult = userService.findPage(params, pageQuery);
        return jsonResult;
    }*/
}
