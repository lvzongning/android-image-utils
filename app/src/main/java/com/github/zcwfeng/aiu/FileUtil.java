package com.github.zcwfeng.aiu;

import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class FileUtil {
    public static String FILE_PATH;
    public static String DOWN_FILE_PATH;
    public static String TEMP_IMG_PATH;
    public final static String DOWNLOAD_TP_PATH = "/template/";
    public static Comparator<String> comparator = null;

    public static ArrayList<String> getFilePathList(File parent, FileFilter ff) {
        ArrayList<String> list = new ArrayList<String>();
        File[] files = parent.listFiles(ff);
        if (files == null) return list;
        for (File file : files) {
            String path = file.getAbsolutePath();
            list.add(path);
        }
        if (list.size() > 1) {
            Collections.sort(list, getComparator());
        }
        return list;
    }


    /**
     * 把字节数组保存为一个文件
     */
    public static File saveFileFromBytes(byte[] b, String outputFile) {
        BufferedOutputStream stream = null;
        File file = null;
        try {
            file = new File(outputFile);
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }


    public static Comparator<String> getComparator() {
        if (comparator == null) {
            comparator = new Comparator<String>() {
                @Override
                public int compare(String lhs, String rhs) {
                    return Collator.getInstance(Locale.CHINA).compare(rhs, lhs);
                }
            };
        }
        return comparator;
    }


    public static boolean fileEmpty(String path) {
        File file = new File(path);
        return file.length() == 0;
    }


    public static void checkPath() {
        FILE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + File.separator + "yytsp";
        DOWN_FILE_PATH = FILE_PATH + File.separator + "Download";
        TEMP_IMG_PATH = FILE_PATH + File.separator + "TempImg";

        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(FILE_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(DOWN_FILE_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(TEMP_IMG_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static byte[] readFileToBytes(File file) {
        try {
            return readStreamToBytes(new FileInputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static byte[] readStreamToBytes(InputStream in) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 8];
        int length = -1;
        while ((length = in.read(buffer)) != -1) {
            out.write(buffer, 0, length);
        }
        out.flush();
        byte[] result = out.toByteArray();
        in.close();
        out.close();
        return result;
    }


    /**
     * 使用FileWriter实现文件的追加，其中：fileName表示文件名；content表示要追加的内容
     *
     * @param fileName
     * @param content
     * @param flag
     */
    public static void appendTextMethod(String fileName, String content, boolean flag) {
        try {
            File file = new File(fileName);
            if (file.isFile() && !file.exists()) {
                file.createNewFile();
            }
            // 创建一个FileWriter对象，其中boolean型参数则表示是否以追加形式写文件
            FileWriter fw = new FileWriter(fileName, flag);
            // 追加内容
            fw.write("\n" + content);
            // 关闭文件输出流
            fw.close();


//            if (file.getFreeSpace() >= Constant.STATISTICAL_FILE_LIMIT) {
//                UploadUtils.uploadFile();
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * del statictic file
     */
//    public static void delStatisticFile() {
//        try {
//            File file = new File(Constant.STATISTICAL_FILENAME);
//            if (file.exists()) {
//                file.delete();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

    /**
     * del 动态删除 file
     */
    public static void delStatisticFile(String filepath) {
        try {
            File file = new File(filepath);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 测试读取内容
     *
     * @param fileName
     */
    public static void showFileContent(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }
}
