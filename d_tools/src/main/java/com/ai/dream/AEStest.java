package com.ai.dream;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.sound.midi.Soundbank;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.sql.SQLOutput;
import org.apache.commons.codec.binary.Base64;

public class AEStest {

    /*// 加密
    public static String Encrypt(String sKey, String sSrc) throws Exception {
        if (sKey == null) {
            System.out.print("Key为空null");
            return null;
        }
        String byte16 = new String(Base64.getEncoder().encode(sKey.getBytes()),"utf-8").substring(0,16);
        //String byte16 = sKey.substring(0,16);
        System.out.println(byte16);
        // 判断Key是否为16位
        if (byte16.length() != 16) {
            return null;
        }
        byte[] raw = byte16.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));

        String str = Base64.getEncoder().encodeToString(encrypted);
        System.out.println(str);
        return URLEncoder.encode(str);
    }
*/

    /*// 解密
    public static String Decrypt(String sKey, String sSrc) throws Exception {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                System.out.print("Key为空null");
                return null;
            }
            sKey = "FUSE2019-11-14 00:01:12";
            //String byte16 = new String(Base64.getEncoder().encode(sKey.getBytes()),"utf-8").substring(0,16);
            String byte16 =new Base64().encodeToString(sKey.getBytes("utf-8")).substring(0,16);
            //new String(base64Key,"utf-8");

            System.out.println(byte16);
            //String byte16 = sKey.substring(0,16);
            // 判断Key是否为16位
            if (byte16.length() != 16) {
                System.out.print("Key长度不是16位");
                return null;
            }

            byte[] raw = byte16.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);

            byte[] encrypted1 = new Base64().decode(URLDecoder.decode(sSrc));//先通过HTML解码再BASE64解码
            System.out.println(new String(encrypted1,"utf-8"));
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original,"utf-8");//最终解密的结果
                return originalString;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }*/

    // 解密
    public static String Decrypt(String sSrc, String sKey) throws Exception {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                System.out.print("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                System.out.print("Key长度不是16位");
                return null;
            }
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = new Base64().decode(sSrc);//先用base64解密
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original,"utf-8");
                return originalString;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }



    public static void main(String[] args) throws Exception{

        //AES加密串,解密
        String key = "FUSE2019-11-14 00:01:12";
        String base64Key = java.util.Base64.getEncoder().encodeToString(key.getBytes()).substring(0,16);

        String urldecodered = URLDecoder.decode("@[APP]mDZbdQkfs5FLUlGFBdVvNbS5zc0bMuqQeipaaV0WrHk%3D".substring(6));
        //byte[] encrypted1 = Base64.getMimeDecoder().decode(urldecodered);

        String DesDecryptBusiness = new AEStest().Decrypt(urldecodered,base64Key);



        //aes加密
        //System.out.println("加密+64转码结果："+new AEStest().Encrypt("FUSE2019-11-12 12:03:54","job"));
        //aes解密
        System.out.println("解密结果："+new AEStest().Decrypt("FUSE2019-11-14 00:01:12","mDZbdQkfs5FLUlGFBdVvNbS5zc0bMuqQeipaaV0WrHk%3D"));
        //DES
        //System.out.println(new AEStest().encodeDES("FUSE2019-11-12 12:03:54","asdipoqeapasdjasdad"));
        //System.out.println("加密结果："+new AEStest().AESEncode("FUSE2019-11-12 12:03:54","asdipoqeapahsdjasdad"));
        //System.out.println("加密结果："+new AEStest().AESEncode("aaaaaaaaaaaaaaaa","asdipoqeapahsdjasdad"));
        //System.out.println("加密结果："+new AEStest().Encrypt("FUSE2019-11-12 12:03:54","asdipoqeapasdjasdad"));

    }

}
