package com.wendadawen.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

public class FileUtils {
    public static String readBinaryFile(String filePath) throws IOException {
        // 创建File对象，指向要读取的文件
        File file = new File(filePath);

        // 读取文件名
        String fileName = file.getName();

        // 读取二进制数据
        byte[] fileData = Files.readAllBytes(file.toPath());
        String fileDataString = Base64.getEncoder().encodeToString(fileData);;

        return fileName + "||" + fileDataString;
    }
    public static void saveDataToFile(String fileName, String fileData) {
        File folder = new File("file");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        try (FileOutputStream fos = new FileOutputStream("file/"+fileName)) {
            fos.write(Base64.getDecoder().decode(fileData));
            System.out.println("Data saved to file: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
