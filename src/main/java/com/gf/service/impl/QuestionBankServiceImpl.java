package com.gf.service.impl;

import com.gf.entity.QuestionBank;
import com.gf.mapper.QuestionBankMapper;
import com.gf.service.IQuestionBankService;
import com.gf.utils.JsonResult;
import com.gf.utils.PageRequest;
import com.gf.utils.PageResult;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author JusChui
 * @ClassName QuestionBankServiceImpl.java
 * @Date 2021年04月12日 20:25:00
 * @Description
 */
@Service
public class QuestionBankServiceImpl implements IQuestionBankService {

    private Logger logger = Logger.getLogger(QuestionBankServiceImpl.class);

    @Autowired
    private QuestionBankMapper questionBankMapper;

    @Autowired
    DataSourceTransactionManager dataSourceTransactionManager;

    @Autowired
    TransactionDefinition transactionDefinition;

    @Override
    public JsonResult getQuestions(Map<String, Object> params, PageRequest pageRequest) {
        JsonResult jsonResult = new JsonResult();
        logger.info("QuestionBankServiceImpl_getQuestions执行...");
        PageResult pageResult;
        try {
            Map<String, Object> bean = new HashMap<>();
            pageResult = PageUtils.getPageResult(pageRequest, getPageInfo(params, pageRequest));
            //System.out.println(pageResult.getContent().toString());
            bean.put("total", pageResult.getTotalSize());   //记录总数
            jsonResult.setRtCode(200);
            jsonResult.setRtMsg("查询成功");
            jsonResult.setBean(bean);
            jsonResult.setData(pageResult.getContent());
        } catch (Exception e) {
            jsonResult.setRtCode(500);
            jsonResult.setRtMsg("服务器出错,查询失败");
            logger.info("QuestionBankServiceImpl_getQuestions异常"
                    + e.getMessage() == null ? e.toString() : e.getMessage());
        }
        Gson gson = new Gson();
        logger.info("QuestionBankServiceImpl_getQuestions出参-->" + gson.toJson(jsonResult));
        return jsonResult;
    }

    @Override
    public JsonResult insertQuestionsByFile(List<Map<String, Object>> params) {
        Gson gson = new Gson();
        JsonResult jsonResult = new JsonResult();
        TransactionStatus transactionStatus = null;
        logger.info("QuestionBankServiceImpl_insertQuestionsByFile入参-->" + gson.toJson(params));
        try {
            //手动开启事务！
            List<Map<String, Object>> choiceList = new ArrayList<>();

            transactionStatus =
                    dataSourceTransactionManager.getTransaction(transactionDefinition);
            questionBankMapper.saveQuestionBankByExcel(params);
            questionBankMapper.saveRightChoiceByExcel(params);
            for (int i = 0; i < params.size(); i++) {
                Map<String, Object> objectMap = params.get(i);
                String questionId = null;
                for (Map.Entry map : objectMap.entrySet()
                ) {
                    Map<String, Object> choiceMap = new HashMap<>();
                    if (StringUtils.equals("id", (String) map.getKey())) {
                        questionId = (String) map.getValue();
                    }
                    choiceMap.put("questionId", questionId);
                    if (StringUtils.equals("A", (String) map.getKey())) {
                        choiceMap.put("choiceName", "A");
                        choiceMap.put("choice", map.getValue());
                        choiceList.add(choiceMap);
                    }
                    if (StringUtils.equals("B", (String) map.getKey())) {
                        choiceMap.put("choiceName", "B");
                        choiceMap.put("choice", map.getValue());
                        choiceList.add(choiceMap);
                    }
                    if (StringUtils.equals("C", (String) map.getKey())) {
                        choiceMap.put("choiceName", "C");
                        choiceMap.put("choice", map.getValue());
                        choiceList.add(choiceMap);
                    }
                    if (StringUtils.equals("D", (String) map.getKey())) {
                        choiceMap.put("choiceName", "D");
                        choiceMap.put("choice", map.getValue());
                        choiceList.add(choiceMap);
                    }
                }
            }
            /*choiceList.forEach(stringObjectMap -> {
                System.out.println(stringObjectMap);
            });*/
            questionBankMapper.saveQuestionChoiceByExcel(choiceList);
            //int a = 1 / 0;
            dataSourceTransactionManager.commit(transactionStatus);//提交事务
            jsonResult.setRtCode(200);
            jsonResult.setRtMsg("导入成功");
        } catch (Exception e) {
            dataSourceTransactionManager.rollback(transactionStatus);
            jsonResult.setRtCode(500);
            jsonResult.setRtMsg("服务器出错,导入失败");
            logger.info("QuestionBankServiceImpl_insertQuestionsByFile异常"
                    + e.getMessage() == null ? e.toString() : e.getMessage());
        }
        logger.info("QuestionBankServiceImpl_insertQuestionsByFile出参-->" + gson.toJson(jsonResult));
        return jsonResult;
    }

    /**
     * 调用分页插件完成分页
     *
     * @param pageRequest
     * @return
     */
    private PageInfo<QuestionBank> getPageInfo(Map<String, Object> params, PageRequest pageRequest) {
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<QuestionBank> sysMenus = questionBankMapper.getAllQuestion(params);
        return new PageInfo<QuestionBank>(sysMenus);
    }
}
