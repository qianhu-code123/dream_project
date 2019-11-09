package com.ai.dream.service.interfaces;

import com.ai.dream.collect.netCollection;

import java.util.List;
import java.util.Map;

public interface ICollectionSV {

     List<netCollection> queryAll();

     boolean addnew(netCollection collection);

     boolean delById(long id);

     boolean updnew(netCollection collection);

}
