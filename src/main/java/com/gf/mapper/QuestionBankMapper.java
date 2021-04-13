package com.gf.mapper;

import com.gf.entity.QuestionBank;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QuestionBankMapper {

    List<QuestionBank> getAllQuestion(Map<String, Object> params);
}
