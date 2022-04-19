package com.czarnowski.client;

import com.czarnowski.server.FileStorage;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Client client = new Client();
        client.createDirs();
        FileStorage.makeDirs();

        Scanner scanner = new Scanner(System.in);
        System.out.println("System.out.println(Enter action " +
                "(1 - get a file, 2 - save a file, 3 - delete a file):\n");
        boolean exit = false;
        while (!exit) {
            String action = scanner.nextLine();
            switch (action) {
                case "1" -> client.getFile();
                case "2" -> client.putFile();
                case "3" -> client.deleteFile();
                case "4" -> client.showFiles();
                case "exit" -> {
                    exit = true;
                    client.shutdownServer();
                }
                default -> System.out.println("Wrong command");
            }
        }
    }
}
