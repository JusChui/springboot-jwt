package com.gf.controller;

import com.gf.service.IQuestionBankService;
import com.gf.utils.JsonResult;
import com.gf.utils.PageRequest;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JusChui
 * @ClassName QuestionController.java
 * @Date 2021年04月12日 20:35:00
 * @Description
 */
@RestController
public class QuestionController {

    @Autowired
    private IQuestionBankService questionBankService;

    private Logger logger = Logger.getLogger(QuestionController.class);

    @RequestMapping("/question/getQuestions")
    @ResponseBody
    public JsonResult getQuestions(@RequestBody Map<String, Object> params, PageRequest pageQuery) {
        JsonResult jsonResult = null;
        Integer pageSize = (Integer) params.get("pageSize");    //每页数量
        Integer pageNum = (Integer) params.get("currentPage");  //当前页码
        pageQuery.setPageNum(pageNum);
        pageQuery.setPageSize(pageSize);
        jsonResult = questionBankService.getQuestions(params, pageQuery);
        return jsonResult;
    }

    @RequestMapping("/question/downloadQuestions")
    @ResponseBody
    public JsonResult downloadQuestions(@RequestBody Map<String, Object> params) {
        logger.info("QuestionController_downloadQuestions入参" + params);
        JsonResult jsonResult = new JsonResult();
        try {
            List<Map<String, Object>> questionList = (List<Map<String, Object>>) params.get("params");
            questionList.forEach(stringObjectMap -> {
                System.out.println(stringObjectMap);
            });
        } catch (Exception e) {

        }
        logger.info("QuestionController_downloadQuestions出参" + new Gson().toJson(jsonResult));
        return jsonResult;
    }
}
