package com.ai.dream.shall.service.interfaces;

import java.util.List;
import java.util.Map;

public interface IResourceShallSV {

    boolean  saveResource(Map<String,Object> paraMap) throws Exception;

    boolean  saveResUser(Map<String,Object> paraMap) throws Exception;

    Map<String,Object>   queryResource(Map<String,Object> paraMap) throws Exception;

    Map<String,Object>  queryResUser(Map<String,Object> paraMap) throws Exception;

    List<Map<String,Object>> queryAllResource() throws Exception;

    boolean  updateResource(Map<String,Object> paraMap) throws Exception;

    boolean  updateResUser(Map<String,Object> paraMap) throws Exception;

}
