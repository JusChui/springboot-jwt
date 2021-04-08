package com.gf.utils;

/**
 * @author JusChui
 * @ClassName PageRequest.java
 * @Date 2021年04月08日 22:41:00
 * @Description
 */
public class PageRequest {

    /**
     * 当前页码
     */
    private int pageNum;
    /**
     * 每页数量
     */
    private int pageSize;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
