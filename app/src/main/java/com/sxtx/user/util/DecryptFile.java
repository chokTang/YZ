package com.sxtx.user.util;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 解密图片文件
 *
 * @author BY
 * @date 2019-08-23
 */
public class DecryptFile {


    /**
     * 获取数据流内容
     *
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static InputStream getInPustream(File file, int key) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            int len = 0;
            while ((len = inputStream.read()) != -1) {
                baos.write(len);
            }
            baos.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] result = baos.toByteArray();
        for (int i = 0; i < 100; i++) {
            if (i >= result.length) {
                break;
            }
            result[i] = (byte) (result[i] ^ key);
        }
        return new ByteArrayInputStream(result);
    }

    /**
     * 获取数据流内容
     *
     * @return
     * @throws FileNotFoundException
     */
    public static InputStream getInPustream(InputStream inputStream, int key) throws FileNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            int len = 0;
            while ((len = inputStream.read()) != -1) {
                baos.write(len);
            }
            baos.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] result = baos.toByteArray();
        for (int i = 0; i < 100; i++) {
            if (i >= result.length) {
                break;
            }
            result[i] = (byte) (result[i] ^ key);
        }
        return new ByteArrayInputStream(result);
    }

}
