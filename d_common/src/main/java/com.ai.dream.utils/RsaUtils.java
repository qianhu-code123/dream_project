package com.ai.dream.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
public class RsaUtils {


    private static final String RSA_ALGORITHM = "RSA";
    private static final int MAX_DECRYPT_BLOCK = 128;
    private static  RSAPublicKey publicKey;
    private static  RSAPrivateKey privateKey;

    public static RsaUtils instance(){
        return new RsaUtils();
    }


    public void geneKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM, new BouncyCastleProvider());
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        privateKey = (RSAPrivateKey) keyPair.getPrivate();
        publicKey = (RSAPublicKey)keyPair.getPublic();
    }


    public String encodeByPrivateKey(String body) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(body.getBytes("utf-8")));
    }

    public String encodeByPublicKey(String body)  throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(body.getBytes("utf-8")));
    }


    public String decodeByPrivateKey(String body)   throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return decryptByPrivateKey(body);
    }


    public String decodeByPublicKey(String body)    throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(body)));
    }


    /**
     * 私钥解密
     *
     * @param encryptedStr
     * @return
     */
    public  String decryptByPrivateKey(String encryptedStr)
    {
        try
        {
            // 对私钥解密
            byte[] privateKeyBytes = privateKey.getEncoded();
            // 获得私钥
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            // 获得待解密数据
            byte[] data = decryptBase64(encryptedStr);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = factory.generatePrivate(keySpec);
            // 对数据解密
            Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            // 返回UTF-8编码的解密信息
            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0)
            {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK)
                {
                    cache = cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
                } else
                {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return new String(decryptedData, "UTF-8");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * BASE64 解码
     *
     * @param key 需要Base64解码的字符串
     * @return 字节数组
     */
    public static byte[] decryptBase64(String key)
    {
        return Base64.getDecoder().decode(key);
    }


    public static void main(String[] args) throws Exception {
        RsaUtils rsaUtils = new RsaUtils();
        rsaUtils.geneKeys();
        String encodes = rsaUtils.encodeByPrivateKey("123456");
        System.out.println("加密前:"+123456);
        System.out.println("公钥:"+Base64.getEncoder().encodeToString(RsaUtils.publicKey.getEncoded()));
        System.out.println("私钥:"+Base64.getEncoder().encodeToString(RsaUtils.privateKey.getEncoded()));
        System.out.println("私钥加密后:"+encodes);
        System.out.println("公钥加密后:"+rsaUtils.encodeByPublicKey("123456"));
        System.out.println("公钥解密后:"+rsaUtils.decodeByPublicKey(encodes));
    }
}