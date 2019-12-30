package com.sxtx.user.util;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * txt文件内容管理器
 */
public class TXTManager {

    public static String rootXMLPath = Environment.getExternalStorageDirectory().getPath() + "/testTXT";

    /**
     * 保存内容到TXT文件中
     *
     * @param fileName
     * @param content
     * @return
     */
    public static boolean writeToXML(String fileName, String content) {
        FileOutputStream fileOutputStream;
        BufferedWriter bufferedWriter;
        createDirectory(rootXMLPath);
        File file = new File(rootXMLPath + "/" + fileName + ".txt");
        try {
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            bufferedWriter.write(content);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 读取XML内容
     *
     * @param filePath
     * @return
     */
    public static String readFromXML(String filePath) {
        FileInputStream fileInputStream;
        BufferedReader bufferedReader;
        StringBuilder stringBuilder = new StringBuilder();
        File file = new File(filePath);
        if (file.exists()) {
            try {
                fileInputStream = new FileInputStream(file);
                bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                bufferedReader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 创建文件夹
     *
     * @param fileDirectory
     */
    public static void createDirectory(String fileDirectory) {
        File file = new File(fileDirectory);
        if (!file.exists()) {
            file.mkdirs();
        }
    }



    public static Set<String> parseChannels(final File file) throws IOException {
        final List<String> channels = new ArrayList<>();
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        while ((line = br.readLine()) != null) {
            String parts[] = line.split("#");
            if (parts.length > 0) {
                final String ch = parts[0].trim();
                if (ch.length() > 0) {
                    channels.add(ch);
                }
            }
        }
        br.close();
        fr.close();
        return escape(channels);
    }


    public static Set<String> escape(Collection<String> cs) {
        // filter invalid chars for filename
        Pattern p = Pattern.compile("[\\\\/:*?\"'<>|]");
        Set<String> set = new HashSet<>();
        for (String s : cs) {
            set.add(p.matcher(s).replaceAll("_"));
        }
        return set;
    }


}
