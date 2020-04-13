package com.ai.dream.worknote.dao;

import java.util.List;
import java.util.Map;

public interface WorknoteMapper {

    /**
     * 存储笔记
     * @param paraMap
     * @return
     * @throws Exception
     */
    boolean saveNote(Map<String,Object> paraMap) throws Exception;

    /**
     * 初始化所有笔记根据type分类
     * @return
     * @throws Exception
     */
    List<Map<String,Object>> initAllNote(Map<String, Object> paraMap) throws Exception;
    /**
     * 根据title模糊查询笔记
     * @param paramMap
     * @return
     * @throws Exception
     */
    List<Map<String,Object>> queryByTitle(Map<String,Object> paramMap) throws Exception;



}
