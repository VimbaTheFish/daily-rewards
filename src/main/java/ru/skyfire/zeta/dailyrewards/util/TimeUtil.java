package ru.skyfire.zeta.dailyrewards.util;

import ninja.leaping.configurate.ConfigurationNode;
import ru.skyfire.zeta.dailyrewards.DailyRewards;

import java.io.IOException;

public class TimeUtil {
    public static void setCurrentTimeToConfig(){
        DailyRewards.getInst().getRootTimeNode().getNode("time").setValue(System.currentTimeMillis());
        try {
            DailyRewards.getInst().getTimeConfigLoader().save(DailyRewards.getInst().getRootTimeNode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void timeCheck(){
        ConfigurationNode timeNode = DailyRewards.getInst().getRootTimeNode();
        double lastDayTime = timeNode.getNode("time").getDouble();
        double currentTime = System.currentTimeMillis();
        double oneDay = 24*60*60*1000;

        if(lastDayTime<currentTime-oneDay){
            while (currentTime-oneDay>lastDayTime){
                lastDayTime=lastDayTime+oneDay;
            }
            timeNode.getNode("time").setValue(lastDayTime);
            CmdUtil.setNewDay();
        }
    }
}
