package com.ai.dream.sec.service.impl;

import com.ai.dream.sec.mapper.IRegisterMapper;
import com.ai.dream.sec.service.interfaces.IRegisterSV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class registerSVImpl implements IRegisterSV {

    @Autowired
    IRegisterMapper sv;

    @Override
    public Map<String, Object> queryUser(Map<String, Object> paraMap) {
        return sv.queryUser(paraMap);
    }

    @Override
    public List<Map<String, Object>> queryAllUser(Map<String,Object> paraMap) {
        return sv.queryAllUser(paraMap);
    }

    @Override
    public Map<String, Object> queryByUsername(Map<String, Object> paraMap) {
        return sv.queryByUsername(paraMap);
    }

    @Override
    public boolean saveUser(Map<String, Object> paraMap) {
        return sv.saveUser(paraMap);
    }

    @Override
    public boolean deleteUser(List paraMap) {
        return sv.deleteUser(paraMap);
    }

    @Override
    public boolean updateUser(Map<String, Object> paraMap) {
        return sv.updateUser(paraMap);
    }
}
