package com.gf.controller;

import com.gf.entity.User;
import com.gf.utils.PageRequest;
import com.gf.service.IUserService;
import com.gf.utils.JsonResult;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private Logger logger = Logger.getLogger(UserController.class);

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

    @RequestMapping(path = "/user/addToMyStudent", method = {RequestMethod.POST})
    @ResponseBody
    public JsonResult addToMyStudent(@RequestBody List<Map<String, Object>> params, Authentication authentication) {
        logger.info("UserController_addToMyStudent入参-->" + params);
        JsonResult jsonResult = new JsonResult();
        try {
            List list = new ArrayList();
            Map<String, Object> param = new HashMap<>();
            params.forEach(stringObjectMap -> {
                stringObjectMap.forEach((k, v) -> {
                    if (StringUtils.equals(k, "id")) {
                        list.add(v);
                    }
                });
            });
            if (CollectionUtils.isEmpty(list)) {
                jsonResult.setRtCode(200);
                jsonResult.setRtMsg("参数为空");
            } else {
                User user = (User) authentication.getPrincipal();
                //System.out.println(user.getId());
                param.put("tid", user.getId());
                param.put("sids", list);
                logger.info("向service层入参--->" + param);
                userService.add2MyStudent(param);
                jsonResult.setRtCode(200);
                jsonResult.setRtMsg("添加成功");
            }
        } catch (Exception e) {
            jsonResult.setRtCode(500);
            jsonResult.setRtMsg("服务器出错,查询失败");
            logger.info("UserController_addToMyStudent异常-->" + e.getMessage() == null ? e.toString() : e.getMessage());
        }
        logger.info("UserController_addToMyStudent出参-->" + (new Gson()).toJson(jsonResult));
        return jsonResult;
    }

}
