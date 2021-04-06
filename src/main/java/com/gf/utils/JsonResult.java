package com.gf.utils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author JusChui
 * @className JsonResult.java
 * @Date 2021年04月06日 20:07:00
 * @Description
 */
public class JsonResult implements Serializable {

    private Integer rtCode;
    private String rtMsg;
    private Map<String, Object> bean;
    private List<Map<String, Object>> beans;
    private Object data;

    public Integer getRtCode() {
        return rtCode;
    }

    public void setRtCode(Integer rtCode) {
        this.rtCode = rtCode;
    }

    public String getRtMsg() {
        return rtMsg;
    }

    public void setRtMsg(String rtMsg) {
        this.rtMsg = rtMsg;
    }

    public Map<String, Object> getBean() {
        return bean;
    }

    public void setBean(Map<String, Object> bean) {
        this.bean = bean;
    }

    public List<Map<String, Object>> getBeans() {
        return beans;
    }

    public void setBeans(List<Map<String, Object>> beans) {
        this.beans = beans;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
