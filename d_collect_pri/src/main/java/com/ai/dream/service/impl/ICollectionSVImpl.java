package com.ai.dream.service.impl;

import com.ai.dream.collect.netCollection;
import com.ai.dream.mapper.ICollectionMapper;
import com.ai.dream.service.interfaces.ICollectionSV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ICollectionSVImpl implements ICollectionSV {

    @Autowired
    private ICollectionMapper mapper;

    @Override
    public List<netCollection> queryAll() {
        return mapper.queryAll();
    }

    @Override
    public boolean addnew(netCollection collection) {
        return mapper.addnew(collection);
    }

    @Override
    public boolean delById(long id) {
        return mapper.delById(id);
    }

    @Override
    public boolean updnew(netCollection collection) {
        return mapper.updnew(collection);
    }
}
