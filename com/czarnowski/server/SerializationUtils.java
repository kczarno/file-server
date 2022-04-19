package com.czarnowski.server;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtils {


    public static void serialize(Map<Integer, String> filesIndexes, String path, String fileName) {
        new File(path).mkdirs();
        try (ObjectOutputStream out = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(path + fileName)))) {
            out.writeObject(filesIndexes);
        } catch (IOException e) {
            System.out.println("Filed write File indexes into a file " + e);
        }
    }

    public static Map<Integer, String> deserialize(String Path, String fileName) {
        HashMap<Integer, String> mapInFile = new HashMap<>();
        try (ObjectInputStream in = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(Path + fileName)))) {
            mapInFile = (HashMap<Integer, String>) in.readObject();
            System.out.println("deserialized : " + mapInFile);
        } catch (FileNotFoundException e) {
            System.out.println("File not found " + e);
        } catch (IOException e) {
            System.out.println("Failed to read object from file " + e);
        } catch (ClassNotFoundException e) {
            System.out.println("Unknown serialised type " + e);
        }
        return mapInFile;
    }
}
