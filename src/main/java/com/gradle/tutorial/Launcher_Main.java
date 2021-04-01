/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gradle.tutorial;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;

/**
 * @author ammar
 */
public class Launcher_Main extends Application {
    private static Stage applicationMainStage;
    private double xOffset = 0;
    private double yOffset = 0;

    public Launcher_Main() {
    }

    private void setApplicationMainStage(Stage stage) {
        Launcher_Main.applicationMainStage = stage;
    }

    static public Stage getApplicationMainStage() {
        return Launcher_Main.applicationMainStage;
    }

    public static void main(String[] args) throws IOException {

        /*FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getRe ference();
        final StorageReference imageRef = storageRef.child("path/to/file.zip");*/

      //скачивание и распаковка модов в папку .mods
        String urlString = "https://drive.google.com/uc?export=download&confirm=no_antivirus&id=1Rv9wG0ZZUFFhPFupaa-AVfZxYMjxIaOA";
        String username = System.getProperty("user.name");
        String pathSave = "C:\\Users\\" + username + "\\AppData\\Roaming\\.minecraft\\mods\\sborka.zip";
        System.setProperty("http.agent", "Chrome");
      //  String agent = java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("http.agent"));
        File file= new File(pathSave);
        URL url = new URL(urlString);

        FileUtils.copyURLToFile(url, file);
        String destination = "C:\\Users\\"+ username + "\\AppData\\Roaming\\.minecraft\\mods\\";

        try (java.util.zip.ZipFile zipFile = new java.util.zip.ZipFile(file)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                File entryDestination = new File(destination,  entry.getName());
                if (entry.isDirectory()) {
                    entryDestination.mkdirs();
                } else {
                    entryDestination.getParentFile().mkdirs();
                    try (InputStream in = zipFile.getInputStream(entry);
                         OutputStream out = new FileOutputStream(entryDestination)) {
                        IOUtils.copy(in, out);
                    }
                }
            }

        }
        if(file.delete()){
            System.out.println("Был удален");
        }else{
            System.out.println("Нет");
        }
        /*  HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        InputStream in = connection.getInputStream();
        ZipInputStream zipIn = new ZipInputStream(in);
        ZipEntry entry = zipIn.getNextEntry();

        while(entry != null) {

            System.out.println(entry.getName());
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                System.out.println("===File===");

            } else {
                System.out.println("===Directory===");
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();

        }
*/

        /*System.out.println("Getting content for URl : " + urlString);
        url = new URL(urlString);
        uc = url.openConnection();
        uc.addRequestProperty("User-Agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
        uc = url.openConnection();
        uc.connect();
        uc.getInputStream();
        BufferedInputStream in = new BufferedInputStream(uc.getInputStream());
        int ch;
        while ((ch = in.read()) != -1) {
            parsedContentFromUrl.append((char) ch);
        }
        System.out.println(parsedContentFromUrl);
*/
       /* try {
            new FileOutputStream(pathSave).getChannel().transferFrom(Channels.newChannel(new URL(url).openStream()), 0, Long.MAX_VALUE);
            System.out.println("Finish");
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Launcher_Settings.userSettingsLoad();

        //Launcher_Main_Background.setBackgroundImages();

        Parent root = FXMLLoader.load(getClass().getResource("gui/main/Launcher_Main_GUI.fxml"));
        Scene scene = new Scene(root);
        initApplicationSettings(stage, scene);

        stage.setScene(scene);
        stage.show();
    }
    public static  void download_zip_file(URL url, String save_to) {
        try {

            URLConnection conn = url.openConnection();

           conn.addRequestProperty("User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("content-type", "binary/data");
            InputStream in = conn.getInputStream();
            FileOutputStream out = new FileOutputStream(save_to + "tmp.zip");

            byte[] b = new byte[1024];
            int count;

            while ((count = in.read(b)) >= 0) {
                out.write(b, 0, count);
            }
            out.close();
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void initApplicationSettings(Stage stage, Scene scene) {
        setApplicationMainStage(stage);

        stage.getIcons().add(new Image(Launcher_Main.class.getResourceAsStream("/taglauncher_3/css/images/app_icon_1.png")));
        stage.setTitle("Minecraft Launcher");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setMinWidth(450);
        stage.setMinHeight(450);
        stage.setMaxWidth(450);
        stage.setMaxHeight(450);
        stage.setResizable(false);
        Launcher_Settings.setTheme(scene);


        scene.setOnMousePressed(event -> {
            xOffset = stage.getX() - event.getScreenX();
            yOffset = stage.getY() - event.getScreenY();
        });

        scene.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + xOffset);
            stage.setY(event.getScreenY() + yOffset);
        });
    }
}