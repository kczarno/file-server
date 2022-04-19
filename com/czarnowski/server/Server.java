package com.czarnowski.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Server {

    private final String address = "127.0.0.1";
    private final int port = 23456;
    final int poolSize = Runtime.getRuntime().availableProcessors();
    ExecutorService executor = Executors.newFixedThreadPool(poolSize);

    public void start() {
        System.out.println("Server started!");
        loadFilesIndex();

        try (ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(address))) {

            while (true) {
                Session session = new Session(server.accept(), server);
                executor.submit(session);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFilesIndex() {
        Map<Integer, String> indexMap =
                SerializationUtils.deserialize(FileStorage.INDEXES_FILE_PATH, FileStorage.INDEXES_FILE);
        FileStorage.setFilesIndex(indexMap);
        int id = indexMap.keySet().stream().max(Integer::compareTo).orElse(0);
        FileStorage.setFileId(id);
    }
}
