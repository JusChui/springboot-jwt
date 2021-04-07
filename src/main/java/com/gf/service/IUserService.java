package com.gf.service;

import com.gf.utils.JsonResult;

import java.util.Map;

public interface IUserService {

    JsonResult addUser(Map<String, Object> params);
}
