package com.ai.dream.shall.service.impl;

import com.ai.dream.shall.mapper.ResourceShallMapper;
import com.ai.dream.shall.service.interfaces.IResourceShallSV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ResourceShallSVImpl implements IResourceShallSV {

    @Autowired
    private ResourceShallMapper sv;


    @Override
    public boolean saveResource(Map<String, Object> paraMap) throws Exception {
        return sv.saveResource(paraMap);
    }

    @Override
    public boolean saveResUser(Map<String, Object> paraMap) throws Exception {
        return sv.saveResUser(paraMap);
    }

    @Override
    public Map<String, Object> queryResource(Map<String, Object> paraMap) throws Exception {
        return sv.queryResource(paraMap);
    }

    @Override
    public Map<String, Object> queryResUser(Map<String, Object> paraMap) throws Exception {
        return sv.queryResUser(paraMap);
    }

    @Override
    public List<Map<String, Object>> queryAllResource() throws Exception {
        return sv.queryAllResource();
    }

    @Override
    public boolean updateResource(Map<String, Object> paraMap) throws Exception {
        return sv.updateResource(paraMap);
    }

    @Override
    public boolean updateResUser(Map<String, Object> paraMap) throws Exception {
        return sv.updateResUser(paraMap);
    }
}
