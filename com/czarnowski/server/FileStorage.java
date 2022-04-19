package com.czarnowski.server;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


public class FileStorage {

    private static int fileId = 0;
    private static Map<Integer, String> filesIndex = new HashMap<>();

    private static final String FILE_PATH = System.getProperty("user.dir") +
            File.separator + "src" +
            File.separator + "server" +
            File.separator + "data" +
            File.separator;

    static final String INDEXES_FILE_PATH = System.getProperty("user.dir") +
            File.separator + "src" +
            File.separator + "storage" +
            File.separator + "data" +
            File.separator;

    static String INDEXES_FILE = "indexes.data";

    public static void makeDirs() {
        File folder = new File(FILE_PATH);
        folder.mkdirs();
    }

    public static void setFileId(int fileId) {
        FileStorage.fileId = fileId;
    }

    public static void setFilesIndex(Map<Integer, String> filesIndex) {
        FileStorage.filesIndex = filesIndex;
    }

    public static String getFile(Session session, String getterType, String requiredFile) {
        if (getterType.equals("BY_ID")) {
            requiredFile = getFileNameById(Integer.parseInt(requiredFile));
        }
        try {
            session.setSavedContent((Files.readAllBytes(Paths.get(FILE_PATH + requiredFile))));
            return ResponseStatus.OK.getCode() + " ";
        } catch (IOException e) {
            return ResponseStatus.NOT_FOUND.getCode() + " ";
        }
    }

    public static String saveFile(String fileName, byte[] fileContent) {
        File file = new File(FILE_PATH + fileName);
        if (!file.exists()) {
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(fileContent);
                synchronized (FileStorage.class) {
                    ++fileId;
                    filesIndex.put(fileId, fileName);
                    SerializationUtils.serialize(filesIndex, INDEXES_FILE_PATH, INDEXES_FILE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ResponseStatus.OK.getCode() + " " + fileId;
        } else {
            return ResponseStatus.FORBIDDEN.getCode() + "";
        }
    }

    public static String deleteFile(String getterType, String requiredFile) {
        if (getterType.equals("BY_ID")) {
            int id = Integer.parseInt(requiredFile);
            requiredFile = getFileNameById(id);
            filesIndex.remove(id);
        }

        File file = new File(FILE_PATH + requiredFile);
        filesIndex.values().remove(requiredFile);
        return file.delete() ? ResponseStatus.OK.getCode() + ""
                : ResponseStatus.NOT_FOUND.getCode() + "";
    }

    public synchronized static String getFileNameById(int fileId) {
        return filesIndex.getOrDefault(fileId, "no such file");
    }

    public static String showFiles() {
        System.out.println(filesIndex);
        return "";
    }
}