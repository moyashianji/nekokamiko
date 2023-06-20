package com.Moyashi.nekokamiko.time;

import net.minecraft.client.Minecraft;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.Moyashi.nekokamiko.main.Nekokamiko.COLOR_BACKGROUND;

public class inGameTime {

    public static void printTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH/mm/ss/");
        long worldTime = Minecraft.getInstance().level.getDayTime();

        Date date = new Date(worldTime);
        String formattedTime = sdf.format(date);

        System.out.println(COLOR_BACKGROUND+"NowTime:" + formattedTime);
    }
}

