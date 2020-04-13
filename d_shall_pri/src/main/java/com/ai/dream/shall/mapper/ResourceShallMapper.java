package com.ai.dream.shall.mapper;

import com.ai.dream.shall.entity.ResourceShall;

import java.util.List;
import java.util.Map;

public interface ResourceShallMapper {

    boolean  saveResource(Map<String,Object> paraMap) throws Exception;

    boolean  saveResUser(Map<String,Object> paraMap) throws Exception;

    Map<String,Object>   queryResource(Map<String,Object> paraMap) throws Exception;

    Map<String,Object>  queryResUser(Map<String,Object> paraMap) throws Exception;

    List<Map<String,Object>> queryAllResource(Map<String,Object> paraMap) throws Exception;

    boolean  updateResource(Map<String,Object> paraMap) throws Exception;

    boolean  updateResUser(Map<String,Object> paraMap) throws Exception;

    boolean  saveFromExcel(List<ResourceShall> reslist) throws Exception;

    boolean  delResById(List<String> resIdList) throws Exception;

    Map<String,Object> getKuc(Map<String,Object> paraMap) throws Exception;
}
