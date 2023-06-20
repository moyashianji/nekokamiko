package com.Moyashi.nekokamiko.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static com.Moyashi.nekokamiko.main.Nekokamiko.COLOR_BACKGROUND;

public class ConfigFile {

    public void ConfigFile(){

    }
    public static void generateConfigFile(){
        File modsFolder = new File("mods");
        File modFolder = new File(modsFolder, "nekokamiko");

        System.out.println(COLOR_BACKGROUND+"Already File Created");

        //モッド名のフォルダが存在しなければ生成する
        if(!modFolder.exists()){
            modFolder.mkdirs();
            System.out.println(COLOR_BACKGROUND+"Folder Created");
        }

        File configFile = new File(modFolder, "config.txt");
        System.out.println(COLOR_BACKGROUND+"Already Config Created");

        if(!configFile.exists()){
            try{
                configFile.createNewFile();

                FileWriter writer = new FileWriter(configFile);
                writer.write("aaaaaa");
                writer.close();
                System.out.println(COLOR_BACKGROUND+"Config Created");


            } catch (IOException e) {

                e.printStackTrace();
            }

        }
    }

}
