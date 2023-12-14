package com.wendadawen.frame;

import com.wendadawen.utils.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class ChatSystemGUI extends JFrame {
    private static String serverHost = "127.0.0.1";
    private static String serverPort = "10086";
    private Socket client = null;
    private BufferedReader br = null;
    private BufferedWriter bw = null;

    private boolean isConnection = false;
    private String userName;
    private String[] users = {};


    private JTextField usernameField;
    private JButton connectButton;
    private JButton disconnectButton;
    private JList<String> userSelectionList;
    private JTextArea inputTextArea;
    private JPanel messagePanel;
    private String filePath = null;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public static void setServerHost(String serverHost) {
        ChatSystemGUI.serverHost = serverHost;
    }

    public static void setServerPort(String serverPort) {
        ChatSystemGUI.serverPort = serverPort;
    }

    public void setClient(Socket client) {
        this.client = client;
    }

    public void setBr(BufferedReader br) {
        this.br = br;
    }

    public void setBw(BufferedWriter bw) {
        this.bw = bw;
    }

    public boolean isConnection() {
        return isConnection;
    }

    public void setConnection(boolean connection) {
        isConnection = connection;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String[] getUsers() {
        return users;
    }

    public JTextField getUsernameField() {
        return usernameField;
    }

    public void setUsernameField(JTextField usernameField) {
        this.usernameField = usernameField;
    }

    public JButton getConnectButton() {
        return connectButton;
    }

    public void setConnectButton(JButton connectButton) {
        this.connectButton = connectButton;
    }

    public JButton getDisconnectButton() {
        return disconnectButton;
    }

    public void setDisconnectButton(JButton disconnectButton) {
        this.disconnectButton = disconnectButton;
    }

    public JList<String> getUserSelectionList() {
        return userSelectionList;
    }

    public void setUserSelectionList(JList<String> userSelectionList) {
        this.userSelectionList = userSelectionList;
    }

    public JTextArea getInputTextArea() {
        return inputTextArea;
    }

    public void setInputTextArea(JTextArea inputTextArea) {
        this.inputTextArea = inputTextArea;
    }

    public JPanel getMessagePanel() {
        return messagePanel;
    }

    public void setMessagePanel(JPanel messagePanel) {
        this.messagePanel = messagePanel;
    }

    public void setUsers(String[] users){
        this.users = users;
        DefaultListModel<String> model = new DefaultListModel<>();
        for (String user : users) {
            model.addElement(user);
        }
        userSelectionList.setModel(model);
    }

    public static String getServerHost() {
        return serverHost;
    }

    public static String getServerPort() {
        return serverPort;
    }

    public Socket getClient() {
        return client;
    }

    public BufferedReader getBr() {
        return br;
    }

    public BufferedWriter getBw() {
        return bw;
    }

    public String getUserName() {
        return userName;
    }

    public ChatSystemGUI() throws IOException {
        // 设置窗口标题
        super("Chat System");

        // 设置窗口大小
        setSize(600, 400);

        // 创建顶部面板
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        // 创建用户名输入框
        usernameField = new JTextField(10);
        topPanel.add(new JLabel("Username:"));
        topPanel.add(usernameField);

        // 创建连接按钮
        connectButton = new JButton("Connect");
        topPanel.add(connectButton);

        // 创建断开按钮
        disconnectButton = new JButton("Disconnect");
        topPanel.add(disconnectButton);

        // 创建用户选择列表
        userSelectionList = new JList<>(users);
        userSelectionList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION); // 设置选择模式为多选
        JScrollPane userScrollPane = new JScrollPane(userSelectionList);
        userScrollPane.setPreferredSize(new Dimension(100, 80));
        topPanel.add(new JLabel("Send to:"));
        topPanel.add(userScrollPane);

        // 创建选择文件按钮
        JButton selectFileButton = new JButton("Select File");

        // 创建消息输入框
        inputTextArea = new JTextArea(5, 20);

        // 创建接收消息框
        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));

        // 创建发送类型下拉列表框
        String[] sendTypes = {"Text", "Image", "File"};
        JComboBox<String> sendTypeComboBox = new JComboBox<>(sendTypes);
        sendTypeComboBox.setSize(100, 10);
        // 创建发送按钮
        JButton sendButton = new JButton("Send");

        // 创建底部面板
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(sendTypeComboBox, BorderLayout.WEST);
        bottomPanel.add(selectFileButton, BorderLayout.CENTER);
        bottomPanel.add(sendButton,BorderLayout.EAST);
        bottomPanel.add(new JScrollPane(inputTextArea), BorderLayout.PAGE_END);

        // 设置整体布局
        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(messagePanel), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        this.client = new Socket(serverHost, Integer.parseInt(serverPort));
        this.br = new BufferedReader(new InputStreamReader(client.getInputStream()));
        this.bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        // 注册连接按钮的事件监听器
        connectButton.addActionListener(e -> {
            String username = usernameField.getText();
            if(isConnection) {
                JOptionPane.showMessageDialog(null, "已经连接", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if(username.equals("")) {
                // 弹窗提示未输入姓名
                JOptionPane.showMessageDialog(null, "请输入姓名", "提示", JOptionPane.WARNING_MESSAGE);
            } else {
                try {
                    JOptionPane.showMessageDialog(null, "连接成功", "提示", JOptionPane.WARNING_MESSAGE);
                    this.isConnection = true;
                    this.userName = username;
                    clientSend("requestConnect", userName, "server", "", bw);
                }
                catch (IOException ex) {throw new RuntimeException(ex);}
            }
        });

        // 注册断开按钮的事件监听器
        disconnectButton.addActionListener(e -> {
            if(client == null) {
                JOptionPane.showMessageDialog(null, "还没连接", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                clientSend("requestDisConnect", userName, "server", "", bw);
            } catch (IOException ex) {throw new RuntimeException(ex);}
        });

        // 注册选择文件按钮的事件监听器
        selectFileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(ChatSystemGUI.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                this.filePath = fileChooser.getSelectedFile().getAbsolutePath();
            }
        });

        // 注册发送按钮的事件监听器
        sendButton.addActionListener(e -> {
            if(client == null) {
                JOptionPane.showMessageDialog(null, "还没连接", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String message = inputTextArea.getText();
            String selectedSendType = (String) sendTypeComboBox.getSelectedItem();
            String[] revUsers = userSelectionList.getSelectedValuesList().toArray(new String[0]);
            switch (selectedSendType) {
                case "Text":
                    try {
                        if(message.equals("")) {
                            JOptionPane.showMessageDialog(null, "发送消息为空", "提示", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        System.out.println("发送了："+message);
                        clientSend("sendText", userName, arrayToString(revUsers), message, bw);
                    } catch (IOException ex) {throw new RuntimeException(ex);}
                    break;
                case "Image":
                    try {
                        if(this.filePath == null) {
                            JOptionPane.showMessageDialog(null, "未选择图片", "提示", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        System.out.println("发送了一张图片");
                        clientSend("sendImage", userName, arrayToString(revUsers), FileUtils.readBinaryFile(filePath), bw);
                        this.filePath = null;
                    } catch (IOException ex) {throw new RuntimeException(ex);}
                    break;
                case "File":
                    try {
                        if(this.filePath == null) {
                            JOptionPane.showMessageDialog(null, "未选择文件", "提示", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        System.out.println("发送了一个文件");
                        clientSend("sendFile", userName, arrayToString(revUsers), FileUtils.readBinaryFile(filePath), bw);
                        this.filePath = null;
                    } catch (IOException ex) {throw new RuntimeException(ex);}
                    break;
                default:
                    break;
            }

        });

        // 设置窗口关闭时的操作
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 显示窗口
        setVisible(true);
    }

    private static void clientSend(String type, String userName, String revNames, String msg, BufferedWriter bw) throws IOException {
        msg = msg.replaceAll("\r", "wendadawen===>touminghuanhangfu1");
        msg = msg.replaceAll("\n", "wendadawen===>touminghuanhangfu2");
        bw.write(type); bw.newLine();
        bw.write(userName); bw.newLine();
        bw.write(revNames); bw.newLine();
        bw.write(msg); bw.newLine();
        bw.flush();
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
