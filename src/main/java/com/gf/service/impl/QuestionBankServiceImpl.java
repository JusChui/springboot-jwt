package com.gf.service.impl;

import com.gf.entity.QuestionBank;
import com.gf.entity.User;
import com.gf.mapper.QuestionBankMapper;
import com.gf.service.IQuestionBankService;
import com.gf.utils.JsonResult;
import com.gf.utils.PageRequest;
import com.gf.utils.PageResult;
import com.gf.utils.PageUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public JsonResult getQuestions(PageRequest pageRequest) {
        JsonResult jsonResult = new JsonResult();
        logger.info("QuestionBankServiceImpl_getQuestions执行...");
        PageResult pageResult;
        try {
            Map<String, Object> bean = new HashMap<>();
            pageResult = PageUtils.getPageResult(pageRequest, getPageInfo(pageRequest));
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

    /**
     * 调用分页插件完成分页
     *
     * @param pageRequest
     * @return
     */
    private PageInfo<QuestionBank> getPageInfo(PageRequest pageRequest) {
        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<QuestionBank> sysMenus = questionBankMapper.getAllQuestion();
        return new PageInfo<QuestionBank>(sysMenus);
    }
}
