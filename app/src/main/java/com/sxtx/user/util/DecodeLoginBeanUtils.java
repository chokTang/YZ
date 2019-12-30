package com.sxtx.user.util;

import java.io.UnsupportedEncodingException;

public class DecodeLoginBeanUtils {
    public static String  decode(String str){

        String decodeIP = str;
        byte[] b = new byte[0];
        try {
            b = decodeIP.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        for (int j = 0; j < b.length; j++) {
            b[j] ^= 1;
        }

        return new String(b);
    }
}
