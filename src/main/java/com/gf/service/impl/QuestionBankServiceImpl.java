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
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.io.File;
import java.io.FileOutputStream;
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
        PageResult pageResult = null;
        try {
            Map<String, Object> bean = new HashMap<>();
            pageResult = PageUtils.getPageResult(getPageInfo(params, pageRequest));
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
            /*业务代码*/
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
            questionBankMapper.saveQuestionChoiceByExcel(choiceList);
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

    @Override
    public JsonResult exportTemplate() {
        JsonResult jsonResult = new JsonResult();
        logger.info("QuestionBankServiceImpl_exportTemplate执行...");
        try {
            //创建工作簿
            XSSFWorkbook workbook = new XSSFWorkbook();
            //创建工作表
            XSSFSheet sheet = workbook.createSheet("sheet1");
            //创建标题行
            XSSFRow row = sheet.createRow(0);
            String[] title =
                    {"content(题目内容)", "score(分值)", "rightChoice(正确选项)", "A(选项A)", "B(选项B)", "C(选项C)", "D(选项D)"};
            //写入标题行
            for (int i = 0; i < title.length; i++) {
                row.createCell(i).setCellValue(title[i]);
            }
            //写入文件
            workbook.write(
                    new FileOutputStream(
                            new File("D:\\template.xlsx")));
            workbook.close();
            jsonResult.setRtCode(200);
            jsonResult.setRtMsg("模板文件已保存至D:\\template.xlsx");
        } catch (Exception e) {
            jsonResult.setRtCode(500);
            jsonResult.setRtMsg("服务器出错,模板文件下载失败");
            logger.info("QuestionBankServiceImpl_exportTemplate异常"
                    + e.getMessage() == null ? e.toString() : e.getMessage());
        }
        return jsonResult;
    }

    @Override
    public JsonResult downloadQuestions(List<Map<String, Object>> questionList) {
        logger.info("QuestionBankServiceImpl_downloadQuestions入参" + questionList);
        JsonResult jsonResult = new JsonResult();
        String[] titleRow = {"题目编号", "题目分值", "入库时间", "题目内容", "正确选项", "A选项", "B选项", "C选项", "D选项"};
        try {
            //创建工作簿
            XSSFWorkbook workbook = new XSSFWorkbook();
            //创建sheet表
            XSSFSheet sheet = workbook.createSheet("sheet1");
            //写入标题行
            XSSFRow trow = sheet.createRow(0);
            for (int i = 0; i < titleRow.length; i++) {
                trow.createCell(i).setCellValue(titleRow[i]);
            }
            //写入数据
            for (int i = 0; i < questionList.size(); i++) {
                XSSFRow row = sheet.createRow(i + 1);
                Map<String, Object> questionMap = questionList.get(i);
                questionMap.forEach((key, value) -> {
                    int col = 0;
                    if (StringUtils.equals(key, "id")) {
                        row.createCell(0).setCellValue((String) value);
                    }
                    if (StringUtils.equals(key, "score")) {
                        row.createCell(1).setCellValue(String.valueOf(value));
                    }
                    if (StringUtils.equals(key, "creatTime")) {
                        row.createCell(2).setCellValue((String) value);
                    }
                    if (StringUtils.equals(key, "content")) {
                        row.createCell(3).setCellValue((String) value);
                    }
                    if (StringUtils.equals(key, "rightChoice")) {
                        row.createCell(4).setCellValue((String) value);
                    }
                    if (StringUtils.equals(key, "A")) {
                        row.createCell(5).setCellValue((String) value);
                    }
                    if (StringUtils.equals(key, "B")) {
                        row.createCell(6).setCellValue((String) value);
                    }
                    if (StringUtils.equals(key, "C")) {
                        row.createCell(7).setCellValue((String) value);
                    }
                    if (StringUtils.equals(key, "D")) {
                        row.createCell(8).setCellValue((String) value);
                    }
                });
                /*questionMap.forEach((k,v)->{
                    int col = 0;
                    if (StringUtils.equals("id", k)){
                        row.createCell(col++).setCellValue(String.valueOf(v));
                    }
                    if (StringUtils.equals("score", k)){
                        row.createCell(col++).setCellValue(String.valueOf(v));
                    }
                    if (StringUtils.equals("creatTime", k)){
                        row.createCell(col++).setCellValue(String.valueOf(v));
                    }
                    if (StringUtils.equals("content", k)){
                        row.createCell(col++).setCellValue(String.valueOf(v));
                    }
                    if (StringUtils.equals("rightChoice", k)){
                        row.createCell(col++).setCellValue(String.valueOf(v));
                    }
                    if (StringUtils.equals("A", k)){
                        row.createCell(col++).setCellValue(String.valueOf(v));
                    }
                    if (StringUtils.equals("B", k)){
                        row.createCell(col++).setCellValue(String.valueOf(v));
                    }
                    if (StringUtils.equals("C", k)){
                        row.createCell(col++).setCellValue(String.valueOf(v));
                    }
                    if (StringUtils.equals("D", k)){
                        row.createCell(col++).setCellValue(String.valueOf(v));
                    }
                });*/
            }
            //写入文件
            workbook.write(
                    new FileOutputStream(
                            new File("D:\\questions.xlsx")));
            workbook.close();
            jsonResult.setRtCode(200);
            jsonResult.setRtMsg("模板文件已保存至D:\\questions.xlsx");
        } catch (Exception e) {
            jsonResult.setRtCode(500);
            jsonResult.setRtMsg("服务器出错,导出题目失败");
            logger.info("QuestionBankServiceImpl_downloadQuestions异常"
                    + e.getMessage() == null ? e.toString() : e.getMessage());
        }
        logger.info("QuestionBankServiceImpl_downloadQuestions" + new Gson().toJson(jsonResult));
        return jsonResult;
    }

    @Override
    public JsonResult deleteQuestionFromDataBase(List<String> list) {
        logger.info("QuestionBankServiceImpl_deleteQuestionFromDataBase入参" + list);
        JsonResult jsonResult = new JsonResult();
        TransactionStatus transactionStatus = null;
        try {
            //开启事务
            transactionStatus =
                    dataSourceTransactionManager.getTransaction(transactionDefinition);
            questionBankMapper.deleteFromQCByIds(list);
            questionBankMapper.deleteFromQAByIds(list);
            questionBankMapper.deleteFromQBByIds(list);
            /*业务代码*/
            dataSourceTransactionManager.commit(transactionStatus);//提交事务
            jsonResult.setRtCode(200);
            jsonResult.setRtMsg("导入成功");
        } catch (Exception e) {
            dataSourceTransactionManager.rollback(transactionStatus);
            jsonResult.setRtCode(500);
            jsonResult.setRtMsg("服务器出错,导出题目失败");
            logger.info("QuestionBankServiceImpl_deleteQuestionFromDataBase异常"
                    + e.getMessage() == null ? e.toString() : e.getMessage());
        }
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
        return new PageInfo<>(sysMenus);
    }
}
