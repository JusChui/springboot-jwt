package com.gf.controller;

import com.gf.service.IQuestionBankService;
import com.gf.utils.FileUtil;
import com.gf.utils.JsonResult;
import com.gf.utils.PageRequest;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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

    /**
     * 分页加载题库
     *
     * @param params    加载参数
     * @param pageQuery 分页
     * @return JsonResult
     */
    @RequestMapping("/question/getQuestions")
    @ResponseBody
    public JsonResult getQuestions(@RequestBody Map<String, Object> params, PageRequest pageQuery) {
        JsonResult jsonResult = null;
        Integer pageSize = (Integer) params.get("pageSize");    //每页数量
        Integer pageNum = (Integer) params.get("currentPage");  //当前页码
        pageQuery.setPageNum(pageNum);
        pageQuery.setPageSize(pageSize);
        params.remove("pageSize");
        params.remove("currentPage");
        jsonResult = questionBankService.getQuestions(params, pageQuery);
        return jsonResult;
    }

    /**
     * 导出题库
     *
     * @param params 导出参数
     * @return JsonResult
     */
    @RequestMapping(value = "/question/downloadQuestions")
    @ResponseBody
    public JsonResult downloadQuestions(@RequestBody Map<String, Object> params) {
        logger.info("QuestionController_downloadQuestions入参" + params);
        JsonResult jsonResult = null;
        try {
            List<Map<String, Object>> questionList = (List<Map<String, Object>>) params.get("params");
            for (int i = 0; i < questionList.size(); i++) {
                Map<String, Object> answerMap = (Map<String, Object>) questionList.get(i).get("questionAnswer");
                List<Map<String, Object>> choiceMapList = (List<Map<String, Object>>) questionList.get(i).get("choices");
                questionList.get(i).put("rightChoice", answerMap.get("rightChoice"));
                questionList.get(i).remove("questionAnswer");
                for (int j = 0; j < choiceMapList.size(); j++) {
                    Map<String, Object> choiceMap = choiceMapList.get(j);
                    String choiceName = null, choice = null;
                    for (Map.Entry map : choiceMap.entrySet()) {
                        if (StringUtils.equals("choiceName", (String) map.getKey())) {
                            choiceName = map.getValue().toString();
                        }
                        if (StringUtils.equals("choice", (String) map.getKey())) {
                            choice = map.getValue().toString();
                        }
                        if (StringUtils.isNotEmpty(choiceName)) {
                            questionList.get(i).put(choiceName, choice);
                        }
                    }

                }
                questionList.get(i).remove("choices");
                questionList.get(i).remove("null");
            }
            jsonResult = questionBankService.downloadQuestions(questionList);
        } catch (Exception e) {
            logger.info("QuestionBankServiceImpl_exportTemplate异常"
                    + e.getMessage() == null ? e.toString() : e.getMessage());
        }
        //logger.info("QuestionController_downloadQuestions出参" + new Gson().toJson(jsonResult));
        return jsonResult;
    }


    /**
     * 从Excel批量导入题目
     *
     * @param files 要导入的文件
     * @return JsonResult
     * @throws IOException IoException
     */
    @RequestMapping(path = "/question/uploadQuestions")
    @ResponseBody
    public JsonResult uploadQuestions(@RequestParam("file") MultipartFile[] files) throws IOException {
        JsonResult jsonResult = new JsonResult();
        List<Map<String, Object>> list = new ArrayList<>();
        for (MultipartFile f : files
        ) {
            String fileName = f.getOriginalFilename();
            String fileType = fileName.substring(fileName.lastIndexOf("."), fileName.length());
            if (!StringUtils.equals(".xlsx", fileType)) {
                //判断文件格式
                jsonResult.setRtCode(201);
                jsonResult.setRtMsg("所有文件必须为xlsx格式");
                return jsonResult;
            } else {
                InputStream inputStream = f.getInputStream();
                XSSFWorkbook sheets = new XSSFWorkbook(inputStream);
                // 获取第一个表单Sheet
                XSSFSheet sheet = sheets.getSheetAt(0);
                //默认第一行为标题行，i = 0
                XSSFRow titleRow = sheet.getRow(0);
                //System.out.println(sheet.getPhysicalNumberOfRows());
                // 循环获取每一行数据
                for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                    XSSFRow row = sheet.getRow(i);
                    Map<String, Object> map = new LinkedHashMap<>();
                    // 读取每一格内容
                    for (int index = 0; index < row.getPhysicalNumberOfCells(); index++) {
                        XSSFCell titleCell = titleRow.getCell(index);
                        XSSFCell cell = row.getCell(index);
                        cell.setCellType(CellType.STRING);
                        if (cell.getStringCellValue().equals("")) {
                            continue;
                        }
                        String tmp = titleCell.getStringCellValue();
                        tmp = tmp.split("[(,（]")[0];
                        map.put(tmp, cell.getStringCellValue());
                        map.put("id", FileUtil.getFileId());
                    }
                    if (map.isEmpty()) {
                        continue;
                    }
                    list.add(map);
                }
            }
        }
        jsonResult = questionBankService.insertQuestionsByFile(list);
        //jsonResult.setRtCode(200);
        return jsonResult;
    }


    /**
     * 下载导入模板
     *
     * @return
     */
    @RequestMapping(path = "/question/getTemplate")
    @ResponseBody
    public JsonResult exportTemplate() {
        return questionBankService.exportTemplate();
    }

    @RequestMapping(path = "/question/deleteQuestions")
    @ResponseBody
    public JsonResult deleteQuestions(@RequestBody Map<String, Object> params) {
        JsonResult jsonResult;
        List<Map<String, Object>> idList = (List<Map<String, Object>>) params.get("params");
        List<String> ids = new ArrayList<>();
        idList.forEach(stringObjectMap -> {
            ids.add((String) stringObjectMap.get("id"));
        });
        jsonResult = questionBankService.deleteQuestionFromDataBase(ids);
        return jsonResult;
    }
}