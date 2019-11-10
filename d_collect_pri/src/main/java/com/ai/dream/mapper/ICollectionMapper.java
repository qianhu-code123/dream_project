package com.ai.dream.mapper;

import com.ai.dream.collect.netCollection;

import java.util.List;
import java.util.Map;

public interface ICollectionMapper {

    List<Map<String,Object>> queryAll() throws Exception;

    Map<String,Object> queryById(Map<String,Object> parmMap) throws Exception;

    boolean addnew(Map<String,Object> parmMap) throws Exception;

    boolean delById(Map<String,Object> parmMap) throws Exception;

    boolean updnew(Map<String,Object> parmMap) throws Exception;

}
