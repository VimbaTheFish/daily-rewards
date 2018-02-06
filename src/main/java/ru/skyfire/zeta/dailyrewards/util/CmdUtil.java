package ru.skyfire.zeta.dailyrewards.util;

import ru.skyfire.zeta.dailyrewards.DailyRewards;

public class CmdUtil {
    public static void setNewDay(){
        TimeUtil.setCurrentTimeToConfig();
        if (DailyRewards.getInst().hardMode){
            DailyRewards.getInst().getSqlite().clearDaysHard();
        }
        DailyRewards.getInst().getSqlite().clearStatuses();
    }
}
