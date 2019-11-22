package com.ai.dream.sec.mapper;

import java.util.List;
import java.util.Map;

public interface IRegisterMapper {
     boolean saveUser(Map<String,Object> paraMap);

     boolean deleteUser(Map<String,Object> paraMap);

     Map<String,Object> queryUser(Map<String,Object> paraMap);

     List<Map<String,Object>> queryAllUser();

     Map<String,Object> queryByUsername(Map<String,Object> paraMap);
}
