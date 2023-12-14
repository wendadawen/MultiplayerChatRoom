package com.wendadawen.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;
import java.util.Objects;

public class User {
    private String name;
    private Socket client;

    private BufferedWriter bw;
    private BufferedReader br;

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", client=" + client +
                ", bw=" + bw +
                ", br=" + br +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) && Objects.equals(client, user.client) && Objects.equals(bw, user.bw) && Objects.equals(br, user.br);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, client, bw, br);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Socket getClient() {
        return client;
    }

    public void setClient(Socket client) {
        this.client = client;
    }

    public BufferedWriter getBw() {
        return bw;
    }

    public void setBw(BufferedWriter bw) {
        this.bw = bw;
    }

    public BufferedReader getBr() {
        return br;
    }

    public void setBr(BufferedReader br) {
        this.br = br;
    }

    public User(String name, Socket client, BufferedWriter bw, BufferedReader br) {
        this.name = name;
        this.client = client;
        this.bw = bw;
        this.br = br;
    }
}
