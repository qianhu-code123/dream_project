package com.ai.dream.worknote.services.impl;

import com.ai.dream.worknote.services.interfaces.IWorknoteSV;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class WorknoteSVImpl implements IWorknoteSV {
    @Override
    public boolean saveNote(Map<String, Object> paraMap) throws Exception {
        return false;
    }

    @Override
    public List<Map<String, Object>> initAllNote(Map<String, Object> paraMap) throws Exception {
        return null;
    }


    @Override
    public List<Map<String, Object>> queryByTitle(Map<String, Object> paramMap) throws Exception {
        return null;
    }
}
