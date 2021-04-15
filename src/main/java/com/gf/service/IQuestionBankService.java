package com.gf.service;

import com.gf.utils.JsonResult;
import com.gf.utils.PageRequest;

import java.util.List;
import java.util.Map;

public interface IQuestionBankService {

    JsonResult getQuestions(Map<String, Object> params, PageRequest pageRequest);

    JsonResult insertQuestionsByFile(List<Map<String, Object>> params);

    JsonResult exportTemplate();

    JsonResult downloadQuestions(List<Map<String, Object>> questionList);

    JsonResult deleteQuestionFromDataBase(List<String> list);
}
