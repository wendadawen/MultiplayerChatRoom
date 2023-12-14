package com.wendadawen;

import com.wendadawen.frame.ChatSystemGUI;
import com.wendadawen.utils.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        ChatSystemGUI GUI = new ChatSystemGUI();
        while(true) {
            BufferedReader br = GUI.getBr();
            try {
                String type = br.readLine();
                switch (type) {
                    case "updateUser" -> updateUser(GUI);
                    case "sendText" -> revText(GUI);
                    case "sendImage" -> revImage(GUI);
                    case "sendFile" -> revFile(GUI);
                }
            } catch (Exception e) {
                System.exit(0);
            }

        }
    }

    private static void revFile(ChatSystemGUI gui) throws IOException {
        BufferedReader br = gui.getBr();

        String userName = br.readLine();

        String[] parts = getMsg(br).split("\\|\\|", 2);
        String fileName = parts[0];
        String fileData = parts[1];
        FileUtils.saveDataToFile(fileName, fileData);

        JLabel messageLabel = new JLabel(userName + ": 发送了文件"+fileName);

        gui.getMessagePanel().add(messageLabel);
        gui.revalidate();
        gui.repaint();

        System.out.println("收到了一个文件");
    }

    private static void revImage(ChatSystemGUI gui) throws IOException {
        BufferedReader br = gui.getBr();

        String userName = br.readLine();

        String[] parts = getMsg(br).split("\\|\\|", 2);
        String fileName = parts[0];
        String fileData = parts[1];
        FileUtils.saveDataToFile(fileName, fileData);

        JLabel messageLabel1 = new JLabel(userName + ": 发送了一张照片" );
        JLabel messageLabel2 = new JLabel();

        ImageIcon originalIcon = new ImageIcon("file/" + fileName);
        Image originalImage = originalIcon.getImage();
        int originalWidth = originalImage.getWidth(null);
        int originalHeight = originalImage.getHeight(null);
        int desiredWidth = 400; // 设置期望的宽度
        int desiredHeight = (int) ((double) originalHeight / originalWidth * desiredWidth); // 根据宽高比计算期望高度
        Image resizedImage = originalImage.getScaledInstance(desiredWidth, desiredHeight, Image.SCALE_DEFAULT);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        messageLabel2.setIcon(resizedIcon);

        gui.getMessagePanel().add(messageLabel1);
        gui.getMessagePanel().add(messageLabel2);
        gui.revalidate();
        gui.repaint();

        System.out.println("收到了一张图片");
    }

    private static void revText(ChatSystemGUI gui) throws IOException {
        BufferedReader br = gui.getBr();
        String userName = br.readLine();
        String msg = getMsg(br);
        JLabel messageLabel = new JLabel(userName + ": " + msg);
        gui.getMessagePanel().add(messageLabel);
        gui.revalidate();
        gui.repaint();
        System.out.println("收到了："+msg);
    }

    private static void updateUser(ChatSystemGUI gui) throws IOException {
        BufferedReader br = gui.getBr(); br.readLine();
        String[] userNames = getMsg(br).split(" ");
        gui.setUsers(userNames);
    }

    private static String getMsg(BufferedReader br) throws IOException {
        return br.readLine()
                .replaceAll("wendadawen===>touminghuanhangfu1", "\r")
                .replaceAll("wendadawen===>touminghuanhangfu2", "\n");
    }
}