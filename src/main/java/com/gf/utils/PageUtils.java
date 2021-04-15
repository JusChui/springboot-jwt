package com.gf.utils;

import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * @author JusChui
 * @ClassName PageUtils.java
 * @Date 2021年04月08日 22:44:00
 * @Description
 */
public class PageUtils {

    /**
     * 将分页信息封装到统一的接口
     *
     * @param
     * @param pageInfo
     * @return
     */
    public static PageResult getPageResult(PageInfo<?> pageInfo) {
        PageResult pageResult = new PageResult();
        pageResult.setPageNum(pageInfo.getPageNum());
        pageResult.setPageSize(pageInfo.getPageSize());
        pageResult.setTotalSize(pageInfo.getTotal());
        pageResult.setTotalPages(pageInfo.getPages());
        pageResult.setContent(pageInfo.getList());
        return pageResult;
    }
}
