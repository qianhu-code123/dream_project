package com.ai.dream.shall.service.impl;

import com.ai.dream.shall.entity.ResourceShall;
import com.ai.dream.shall.mapper.ResourceShallMapper;
import com.ai.dream.shall.service.interfaces.IResourceShallSV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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
    public boolean saveFromExcel(List<ResourceShall> reslist) throws Exception {
        return sv.saveFromExcel(reslist);
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
    public List<Map<String, Object>> queryAllResource(Map<String,Object> paraMap) throws Exception {
        return sv.queryAllResource(paraMap);
    }

    @Override
    public boolean updateResource(Map<String, Object> paraMap) throws Exception {
        return sv.updateResource(paraMap);
    }

    @Override
    public boolean updateResUser(Map<String, Object> paraMap) throws Exception {
        return sv.updateResUser(paraMap);
    }

    @Override
    public boolean delResById(List<String> resIdList) throws Exception {
        return sv.delResById(resIdList);
    }

    @Override
    public Map<String, Object> getKuc(Map<String, Object> paraMap) throws Exception {
        return sv.getKuc(paraMap);
    }


}
