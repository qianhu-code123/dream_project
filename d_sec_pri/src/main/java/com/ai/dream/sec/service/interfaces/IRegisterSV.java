package com.ai.dream.sec.service.interfaces;

import java.util.List;
import java.util.Map;

public interface IRegisterSV {

    Map<String,Object> queryUser(Map<String,Object> paraMap);

    List<Map<String,Object>> queryAllUser(Map<String,Object> paraMap);

    Map<String,Object> queryByUsername(Map<String,Object> paraMap);

     boolean saveUser(Map<String,Object> paraMap);

     boolean deleteUser(List str);

     boolean updateUser(Map<String,Object> paraMap);
}
