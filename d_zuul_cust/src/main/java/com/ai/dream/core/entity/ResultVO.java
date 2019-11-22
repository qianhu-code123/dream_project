package com.ai.dream.core.entity;

import lombok.Data;

@Data
public class ResultVO {

    private Integer code;

    private String msg;

    public ResultVO(){}

    public ResultVO(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }







}
