package com.wendadawen;

import com.wendadawen.model.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Main {
    // 客服端每次发送消息格式：
    // 第一行：消息类型（requestConnect, requestDisConnect, sendText, sendImage, sendFile）
    // 第二行：用户姓名
    // 第三行：接收者姓名，用空格分割
    // 第四行：消息具体(如果是图片或者文件：||作为分隔符，前面是文件名，后面是二进制数据)

    // 服务端每次发送消息的格式：
    // 第一行：消息类型（updateUser，sendText, sendImage, sendFile）
    // 第二行：谁发送给你的
    // 第三行：具体消息
    private static HashMap<String, User> users = new HashMap<>();
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(10086);
        while(true) {
            Socket client = server.accept();
            new Thread(()->{
                try {serverControl(client);}
                catch (IOException ignored) {}
            }).start();
        }
    }
    private static void serverControl(Socket client) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
        while (true) {
            String type = br.readLine();
            String userName = br.readLine();
            User user;
            if(!users.containsKey(userName)) {
                br.readLine(); br.readLine();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                user = new User(userName, client, bw, br);
                users.put(userName, user);
                updateUserList();
                System.out.println(user);
            } else {
                user = users.get(userName);
            }
            switch (type) {
                case "requestDisConnect" -> requestDisConnect(user);
                case "sendText" -> sendText(user);
                case "sendImage" -> sendImage(user);
                case "sendFile" -> sendFile(user);
            }
        }
    }

    private static void requestDisConnect(User user) throws IOException {
        String userName = user.getName();
        user.getClient().close();
        users.remove(userName);
        updateUserList();
    }

    private static void serverSend(String type, String who, String msg, BufferedWriter bw) throws IOException {
        bw.write(type); bw.newLine();
        bw.write(who); bw.newLine();
        bw.write(msg); bw.newLine(); bw.flush();
    }
    private static void updateUserList() throws IOException {
        String[] userNames = users.keySet().toArray(new String[0]);
        String msg = arrayToString(userNames);
        for (String userName : userNames) {
            BufferedWriter bw = users.get(userName).getBw();
            serverSend("updateUser", "server", msg, bw);
        }
    }

    private static void sendText(User user) throws IOException {
        BufferedReader br = user.getBr();
        String userName = user.getName();
        String[] revNames = br.readLine().split(" ");
        String message = br.readLine();
        for (String revName : revNames) {
            if(revName.equals("")) continue;
            BufferedWriter bw = users.get(revName).getBw();
            serverSend("sendText", userName, message, bw);
        }
    }
    private static void sendImage(User user) throws IOException {
        BufferedReader br = user.getBr();
        String userName = user.getName();
        String[] revNames = br.readLine().split(" ");
        String message = br.readLine();
        for (String revName : revNames) {
            if(revName.equals("")) continue;
            BufferedWriter bw = users.get(revName).getBw();
            serverSend("sendImage", userName, message, bw);
        }
    }
    private static void sendFile(User user) throws IOException {
        BufferedReader br = user.getBr();
        String userName = user.getName();
        String[] revNames = br.readLine().split(" ");
        String message = br.readLine();
        for (String revName : revNames) {
            if(revName.equals("")) continue;
            BufferedWriter bw = users.get(revName).getBw();
            serverSend("sendFile", userName, message, bw);
        }
    }

    private static String arrayToString(String[] arr) {
        StringBuilder ret = new StringBuilder();
        int n = arr.length;
        for(int i = 0; i < n; ++ i) {
            if(i != n-1) ret.append(arr[i]).append(" ");
            else ret.append(arr[i]);
        }
        return ret.toString();
    }

}