package com.gf.service.impl;

import com.gf.utils.PageRequest;
import com.gf.utils.PageResult;
import com.gf.entity.User;
import com.gf.mapper.UserMapper;
import com.gf.service.IUserService;
import com.gf.utils.JsonResult;
import com.gf.utils.PageUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JusChui
 * @ClassName UserServiceImpl.java
 * @Date 2021年04月07日 22:08:00
 * @Description
 */
@Service
public class UserServiceImpl implements IUserService {

    private Logger logger = Logger.getLogger(UserServiceImpl.class);

    private final UserMapper userMapper;

    @Autowired
    DataSourceTransactionManager dataSourceTransactionManager;

    @Autowired
    TransactionDefinition transactionDefinition;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 添加用户信息
     *
     * @param params 用户信息
     * @return json
     */
    @Override
    public JsonResult addUser(Map<String, Object> params) {
        logger.info("UserServiceImpl_addUser入参-->" + params);
        JsonResult jsonResult = new JsonResult();
        TransactionStatus transactionStatus = null;
        try {
            String username = (String) params.get("username");
            String password = (String) params.get("password");
            password = DigestUtils.md5DigestAsHex(password.getBytes());
            params.put("password", password);
            Integer status = Integer.valueOf(params.get("status").toString());
            String email = (String) params.get("email");
            if (StringUtils.isEmpty(username) && StringUtils.isEmpty(email)) {
                //两个都为空
                jsonResult.setRtCode(201);
                jsonResult.setRtMsg("用户名和邮箱不能同时为空");
            } else {
                //两个至少一个不为空
                if (!StringUtils.isEmpty(username)) {
                    //若用户名不为空，则检测用户名是否可用
                    User user1 = userMapper.loadUserByUsername(username);
                    if (null != user1) {
                        //用户名不为空但不可用
                        jsonResult.setRtCode(201);
                        jsonResult.setRtMsg("用户名已被注册");
                        return jsonResult;
                    }
                }
                if (!StringUtils.isEmpty(email)) {
                    //若邮箱不为空，则检测邮箱是否可用
                    Map<String, Object> emailMap = new HashMap<>();
                    emailMap.put("email", email);
                    User user2 = userMapper.loadUserByMap(emailMap);
                    if (null != user2) {
                        //邮箱不为空但不可用
                        jsonResult.setRtCode(201);
                        jsonResult.setRtMsg("邮箱已被注册");
                        return jsonResult;
                    }
                }
                //手动开启事务！
                transactionStatus =
                        dataSourceTransactionManager.getTransaction(transactionDefinition);
                /*业务代码*/
                userMapper.saveUser(params);
                User user = userMapper.loadUserByMap(params);
                Map<String, Object> roleMap = new HashMap<>();
                roleMap.put("user_id", user.getId());
                if (status == 0) {
                    roleMap.put("role_id", 2);  // 教师权限
                } else {
                    roleMap.put("role_id", 1);  // 学生权限
                }
                userMapper.saveRole(roleMap);
                dataSourceTransactionManager.commit(transactionStatus);//提交事务
                jsonResult.setRtCode(200);
                jsonResult.setRtMsg("注册成功");
            }
        } catch (Exception e) {
            dataSourceTransactionManager.rollback(transactionStatus);
            jsonResult.setRtCode(500);
            jsonResult.setRtMsg("服务器出错,注册失败");
            logger.info(e.getMessage() == null ? e.toString() : e.getMessage());
        }
        logger.info("UserServiceImpl_addUser出参-->" + jsonResult);
        return jsonResult;
    }

    /**
     * @param params
     * @param pageRequest 自定义，统一分页查询请求
     * @return
     */
    @Override
    public JsonResult getStudents(Map<String, Object> params, PageRequest pageRequest) {
        JsonResult jsonResult = new JsonResult();
        logger.info("UserServiceImpl_getStudents入参-->" + params);
        PageResult pageResult;
        try {
            pageResult = PageUtils.getPageResult(pageRequest, getPageInfo(params, pageRequest));
            Map<String, Object> bean = new HashMap<>();
            bean.put("total", pageResult.getTotalSize());   //记录总数
            jsonResult.setRtCode(200);
            jsonResult.setRtMsg("查询成功");
            jsonResult.setBean(bean);
            jsonResult.setData(pageResult.getContent());
        } catch (Exception e) {
            jsonResult.setRtCode(500);
            jsonResult.setRtMsg("服务器出错,查询失败");
            logger.info(e.getMessage() == null ? e.toString() : e.getMessage());
        }
        Gson gson = new Gson();
        logger.info("UserServiceImpl_getStudents出参-->" + gson.toJson(jsonResult));
        return jsonResult;
    }

    @Override
    public int add2MyStudent(Map<String, Object> params) {
        return userMapper.add2MyStudent(params);
    }

    /**
     * 调用分页插件完成分页
     *
     * @param pageRequest
     * @return
     */
    private PageInfo<User> getPageInfo(Map<String, Object> params, PageRequest pageRequest) {
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<User> sysMenus = userMapper.loadUsersByMap(params);
        return new PageInfo<>(sysMenus);
    }
}
