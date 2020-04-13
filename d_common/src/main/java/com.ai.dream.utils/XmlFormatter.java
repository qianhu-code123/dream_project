package com.ai.dream.utils;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;


public class XmlFormatter {
    public  static String format(String unformattedXml) {
        try {
            final Document document = parseXmlFile(unformattedXml);
            OutputFormat format = new OutputFormat(document);
            format.setLineWidth(65);
            format.setIndenting(true);
            format.setIndent(4);
            Writer out = new StringWriter();
            XMLSerializer serializer = new XMLSerializer(out, format);
            serializer.serialize(document);
            return out.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Document parseXmlFile(String in) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(in));
            return db.parse(is);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //<xxxxx/> ----->    <xxxxx></xxxxx>
    private static String strTrans(String str){
        try {



        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return "";
    }



    public static void main(String[] args) throws Exception{
        String s = "<temp></temp>";//δ��ʽ��ǰ��xml
       String temS =  format(s);
        System.out.println(temS);
        StringBuffer stringBuffer = new StringBuffer();
        String tstr = new XmlFormatter().format(s);
        String temstr = tstr.replace("</UserOrder>","<UserOrder>");
        String[] arrStr = temstr.split("<UserOrder>");
        String headStr = arrStr[0];
        String middleStr = arrStr[1];
        String endStr = arrStr[2];
        System.out.println(headStr);
        System.out.println("-------------------------------------------");
        middleStr =  new XmlFormatter().format(middleStr).replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>","");
        stringBuffer = stringBuffer.append(headStr).append("<UserOrder>").append(middleStr).append("</UserOrder>").append(endStr);
        System.out.println(stringBuffer);

    }
}
