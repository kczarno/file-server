package com.czarnowski.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Session extends Thread {
    private final Socket socket;
    private byte[] savedContent;
    private ServerSocket server;

    public Session(Socket socketFromServer, ServerSocket server) {
        this.server = server;
        this.socket = socketFromServer;
    }

    public void setSavedContent(byte[] savedContent) {
        this.savedContent = savedContent;
    }

    @Override
    public void run() {
        try (
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            // Request from Client
            String received = input.readUTF();
            byte[] rcvContent = new byte[0];
            if (received.startsWith("exit")) {
                server.close();
            } else if (received.startsWith("put")) {
                int length = input.readInt();
                rcvContent = new byte[length];
                input.readFully(rcvContent, 0, rcvContent.length);
            }
            // Response to Client
            String respond = processRequest(received, rcvContent);
            output.writeUTF(respond);
            if (received.startsWith("get") && respond.startsWith("200")) {
                output.writeInt(savedContent.length);
                output.write(savedContent);
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    public String processRequest(String received, byte[] content) {
        String[] rcvParts = received.split(" ");
        System.out.println(Arrays.toString(rcvParts));
        String requestType = rcvParts[0];
        return switch (requestType) {
            case "get" -> FileStorage.getFile(this, rcvParts[1], rcvParts[2]);
            case "put" -> FileStorage.saveFile(rcvParts[1], content);
            case "delete" -> FileStorage.deleteFile(rcvParts[1], rcvParts[2]);
            case "show" -> FileStorage.showFiles();
            default -> "bad request";
        };
    }
}