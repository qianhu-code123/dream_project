package com.ai.dream.controller;

import com.ai.dream.utils.StringUtils;
import com.ai.dream.utils.XmlFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Component
public class AllToolsController {

    Logger log = LoggerFactory.getLogger(AllToolsController.class);

    @RequestMapping("/tools/format")
    @ResponseBody
    private String getXmlForam(@RequestBody Map<String,Object> paramMap){
        String relStr = "";
        try {
            StringBuffer stringBuffer = new StringBuffer();
            String xmlStr = (String) paramMap.get("xmlStr");
            xmlStr = xmlStr.replace("\"\"","\"").replace("\n","").replace("\"<","<").replace("ProdAtt\"","ProdAtt").replace("A\"ttr","Attr");
            xmlStr = xmlStr.replace("ProdStat ","ProdStat").replace("Prod ","Prod");
            xmlStr = XmlFormatter.format(xmlStr);//--head
            String temstr = xmlStr.replace("</UserOrder>","<UserOrder>");
            String[] arrStr = temstr.split("<UserOrder>");
            String headStr = arrStr[0];
            String middleStr = arrStr[1].replace("\"","").replace("\t","");
            String endStr = arrStr[2];
            middleStr = "<temp>"+middleStr+"</temp>";
            middleStr = StringUtils.xmlTrans(middleStr);
            middleStr = XmlFormatter.format(middleStr);
            middleStr = middleStr.replace("<temp>","").replace("</temp>","");
            middleStr = middleStr.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>","");
            stringBuffer = stringBuffer.append(headStr).append("<UserOrder>").append(middleStr).append("</UserOrder>").append(endStr);
            relStr = stringBuffer.toString();
            log.info(relStr);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return relStr;
    }
}
