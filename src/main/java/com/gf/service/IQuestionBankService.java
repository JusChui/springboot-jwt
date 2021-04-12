package com.gf.service;

import com.gf.utils.JsonResult;
import com.gf.utils.PageRequest;

import java.util.Map;

public interface IQuestionBankService {

    JsonResult getQuestions(PageRequest pageRequest);
}
