package Main;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    static StringBuilder sb = new StringBuilder();
    static String dirGames = "Games";
    static String saveGamesPath = "/Users/Анастасия/Desktop/Games/Games/savegames/";

    public static void main(String[] args){

        ArrayList<String> dirsMain = new ArrayList<>(Arrays.asList("src", "res", "savegames", "temp"));
        ArrayList<String> dirsSrc = new ArrayList<>(Arrays.asList("main", "test"));
        ArrayList<String> filesMain = new ArrayList<>(Arrays.asList("Main.java", "Utils.java"));
        ArrayList<String> dirsRes = new ArrayList<>(Arrays.asList("drawables", "vectors", "icons"));
        ArrayList<String> filesTemp = new ArrayList<>(Arrays.asList("temp.txt"));

        new File(dirGames).mkdir();

        makeDirs(dirGames, dirsMain);
        makeDirs(dirGames + "//src", dirsSrc);
        makeDirs(dirGames + "//res", dirsRes);
        createFiles(dirGames + "//src//main", filesMain);
        createFiles(dirGames + "//temp", filesTemp);
        saveLogFile(dirGames + "//temp//temp.txt");

        GameProgress gameProgress1 = new GameProgress(12, 3, 1,103.56);
        GameProgress gameProgress2 = new GameProgress(10, 6, 5,999.09);
        GameProgress gameProgress3 = new GameProgress(9, 9, 10,2016.23);

        saveGame(saveGamesPath + "//save1.dat", gameProgress1);
        saveGame(saveGamesPath + "//save2.dat", gameProgress2);
        saveGame(saveGamesPath + "//save3.dat", gameProgress3);

        ArrayList<String> saveFiles = new ArrayList<>();
        saveFiles.add(saveGamesPath + "//save1.dat");
        saveFiles.add(saveGamesPath + "//save2.dat");
        saveFiles.add(saveGamesPath + "//save3.dat");

        zipFiles(saveGamesPath + "//saves.zip", saveFiles);
    }
    private static void makeDirs(String dirParent, ArrayList<String> dirs){
        for (String dirName : dirs) {
            sb.append("Директория").append(dirName);
            if (new File(dirParent, dirName).mkdir()){
                sb.append(" создана в директории ");
            } else {
                sb.append(" уже существует в директории ");
            }
        sb
                .append(dirParent)
                .append("\n");
        }
    }
    private static void createFiles(String dirParent, ArrayList<String> fileNames){
        for (String fileName : fileNames) {
            try {
                sb.append("Файл ").append(fileName);
                if (new File(dirParent, fileName).createNewFile()){
                    sb.append(" создан в директории ");
                } else {
                    sb.append(" уже существует в директории ");
                }
                sb.append(dirParent).append("\n");
            } catch (IOException e){
                System.out.println(e.getMessage());
            }
        }
    }
    private static void saveLogFile(String logFileName){
        try (FileWriter fw = new FileWriter(logFileName, true)){
            fw.write(sb.toString() + "\n");
            System.out.println("Лог записан в файл: " + logFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void saveGame(String save, GameProgress gameProgress){
        try (FileOutputStream fileOutputStream = new FileOutputStream(save);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(gameProgress);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void zipFiles(String zipPath, ArrayList<String> filesList) {
        try (ZipOutputStream zipOutStream = new ZipOutputStream(new
                FileOutputStream(zipPath))) {

            for (String filePath : filesList) {
                int i = filePath.lastIndexOf("/") + 1;
                String fileName = filePath.substring(i);

                try (FileInputStream fileInStream = new FileInputStream(filePath)) {
                    ZipEntry entry = new ZipEntry(fileName);
                    zipOutStream.putNextEntry(entry);
                    byte[] buffer = new byte[fileInStream.available()];
                    fileInStream.read(buffer);
                    zipOutStream.write(buffer);
                    zipOutStream.closeEntry();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        for (String saveFile : filesList) {
            new File(saveFile).delete();
        }
    }
}
