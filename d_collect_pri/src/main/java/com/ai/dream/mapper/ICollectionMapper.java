package com.ai.dream.mapper;

import com.ai.dream.collect.netCollection;

import java.util.List;

public interface ICollectionMapper {

    List<netCollection> queryAll();

    boolean addnew(netCollection collection);

    boolean delById(long id);

    boolean updnew(netCollection collection);

}
