package com.ai.dream.service.impl;

import com.ai.dream.collect.netCollection;
import com.ai.dream.mapper.ICollectionMapper;
import com.ai.dream.service.interfaces.ICollectionSV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ICollectionSVImpl implements ICollectionSV {

    @Autowired
    private ICollectionMapper mapper;

    @Override
    public List<Map<String,Object>> queryAll() throws Exception {
        return mapper.queryAll();
    }

    @Override
    public Map<String, Object> queryById(Map<String, Object> parmMap) throws Exception {
        return mapper.queryById(parmMap);
    }

    @Override
    public boolean addnew(Map<String,Object> parmMap) throws Exception {
        return mapper.addnew(parmMap);
    }

    @Override
    public boolean delById(Map<String,Object> parmMap) throws Exception {
        return mapper.delById(parmMap);
    }

    @Override
    public boolean updnew(Map<String,Object> parmMap) throws Exception {
        return mapper.updnew(parmMap);
    }
}
