package com.ai.dream.shall.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Setter
@Getter
@ToString
public class ResourceShall {

    private long resId;

    private String resName;

    private String resUrl;

    private String resCheck;

    private String createDate;

    private String expireDate;

    private String state;

    private String level;

}
