package com.sxtx.user.model.encryption;


import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
*
 *功能描述 AES256加密
 * @author lyh
 * @date
 * @param  * @param null
 * @return
 */
public class Aes256Utils {
//    private static final Logger logger = LoggerFactory.getLogger(Aes256Utils.class);
  //  org.bouncycastle.jcajce.provider.symmetric.AES/CBC/PKCS7Padding
    private static final String CIPHERMODEPADDING = "AES/CBC/PKCS7Padding";// AES/CBC/PKCS7Padding
    private final static String AES = "AES";// AES
    private static final String SHA1PRNG = "SHA1PRNG";//// SHA1PRNG 强随机种子算法, 要区别4.2以上版本的调用方法
//    private static final String BC = "BC";
//    private SecretKeySpec skforAES = null;
//    private static String ivParameter = BuildConfig.ivP;// 密钥默认偏移，可更改

//    private byte[] iv = ivParameter.getBytes();
//    private IvParameterSpec IV;
//    private String  sKey = BuildConfig.sKey;// key必须为16位，可更改为自己的key

//    private static Aes256Utils instance = null;
//
//    public static Aes256Utils getInstance() {
//        if (instance == null) {
//            synchronized (Aes256Utils.class) {
//                if (instance == null) {
//                    instance = new Aes256Utils();
//                }
//            }
//        }
//        return instance;
//    }


    static {
        //new org.bouncycastle.jce.provider.BouncyCastleProvider()
//        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }
    public Aes256Utils() {
//        try {
//            byte[] skAsByteArray = sKey.getBytes("UTF-8");
//            skforAES = new SecretKeySpec(skAsByteArray, "AES");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }



//        IV = new IvParameterSpec(iv);
    }

    // 对密钥进行处理
    private byte[] getRawKey(byte[] seed) throws NoSuchAlgorithmException {
        KeyGenerator kgen = KeyGenerator.getInstance(AES);
        //for android
        SecureRandom sr = SecureRandom.getInstance(SHA1PRNG);
        sr.setSeed(seed);
        kgen.init(128, sr); //256 bits or 128 bits,192bits
        //AES中128位密钥版本有10个加密循环，192比特密钥版本有12个加密循环，256比特密钥版本则有14个加密循环。
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }

    public static String encrypt(String msg,String sKey,String ivParameter) {
        try {
            byte[] skAsByteArray = sKey.getBytes("UTF-8");
            SecretKeySpec  skforAES = new SecretKeySpec(skAsByteArray, "AES");
            byte[] iv = ivParameter.getBytes();
            IvParameterSpec IV = new IvParameterSpec(iv);
            byte[] ciphertext = encrypt(CIPHERMODEPADDING, skforAES, IV, msg.getBytes("UTF-8"));
            String base64_ciphertext = new String(Base64.encodeBase64(ciphertext));
            return base64_ciphertext;
        } catch (UnsupportedEncodingException e) {
            Log.e("aes256加密有异常",e.toString());
        }
        return "";
    }

    public static byte[] encrypt(byte msgData[],String sKey,String ivParameter) {
        try {
            byte[] skAsByteArray = sKey.getBytes("UTF-8");
            SecretKeySpec  skforAES = new SecretKeySpec(skAsByteArray, "AES");
            byte[] iv = ivParameter.getBytes();
            IvParameterSpec IV = new IvParameterSpec(iv);
            byte[] ciphertext = encrypt(CIPHERMODEPADDING, skforAES, IV, msgData);
//            String base64_ciphertext = new String(Base64.encodeBase64(ciphertext));
//            return base64_ciphertext;
//            byte data[] = Base64.encodeBase64(ciphertext);
            return ciphertext;
        } catch (UnsupportedEncodingException e) {
            Log.e("aes256加密有异常",e.toString());
        }
        return new byte[0];
    }

    private static byte[] encrypt(String cmp, SecretKey sk, IvParameterSpec IV,
                           byte[] msg) {
        try {
            Cipher c = Cipher.getInstance(cmp);
            c.init(Cipher.ENCRYPT_MODE, sk, IV);
            return c.doFinal(msg);
        } catch (Exception e) {
            Log.e("aes256加密有异常",e.toString());
        }
        return null;
    }

    public static String decrypt(String msg,String sKey,String ivParameter) {
        try {
            byte[] s = Base64.decodeBase64(msg);
            byte[] skAsByteArray = sKey.getBytes("UTF-8");
            SecretKeySpec  skforAES = new SecretKeySpec(skAsByteArray, "AES");
            byte[] iv = ivParameter.getBytes();
            IvParameterSpec IV = new IvParameterSpec(iv);
            String decrypted = new String(decrypt(CIPHERMODEPADDING, skforAES, IV, s));
            return decrypted;
        } catch (Exception e) {
            Log.e("aes256解密有异常",e.toString());
        }
        return "";
    }

    public static byte[] decrypt(byte msg[],String sKey,String ivParameter) {
        try {
//            byte[] s = Base64.decodeBase64(msg);
            byte[] skAsByteArray = sKey.getBytes("UTF-8");
            SecretKeySpec  skforAES = new SecretKeySpec(skAsByteArray, "AES");
            byte[] iv = ivParameter.getBytes();
            IvParameterSpec IV = new IvParameterSpec(iv);
//            String decrypted = new String(decrypt(CIPHERMODEPADDING, skforAES, IV, s));
//            return decrypted;
            byte data[] = decrypt(CIPHERMODEPADDING, skforAES, IV, msg);
            return data;
        } catch (Exception e) {
            Log.e("aes256解密有异常",e.toString());
        }
        return new byte[0];
    }
    private static byte[] decrypt(String cmp, SecretKey sk, IvParameterSpec IV,
                           byte[] ciphertext) {
        try {
            Cipher c = Cipher.getInstance(cmp);
            c.init(Cipher.DECRYPT_MODE, sk, IV);
            return c.doFinal(ciphertext);
        } catch (Exception e) {
            Log.e("aes256解密有异常",e.toString());
        }
        return null;
    }


    /**
     * md5加密
     */
    public static String encrypt2MD5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }
}
