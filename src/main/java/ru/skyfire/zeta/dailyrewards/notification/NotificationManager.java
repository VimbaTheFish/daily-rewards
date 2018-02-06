package ru.skyfire.zeta.dailyrewards.notification;

import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import ru.skyfire.zeta.dailyrewards.DailyRewards;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static ru.skyfire.zeta.dailyrewards.util.TextUtil.trans;

public class NotificationManager {
    private int time;
    private Map<UUID, Task> taskMap;

    public NotificationManager (){
        ConfigurationNode node = DailyRewards.getInst().getRootDefNode();
        this.time = node.getNode("time-interval").getInt(0);
        this.taskMap = new HashMap<>();
    }

    public void addPlayer(Player player){
        if (time<=0){
            return;
        }
        Task task = Sponge.getScheduler().createTaskBuilder()
                .async()
                .delay(time, TimeUnit.SECONDS)
                .interval(time, TimeUnit.SECONDS)
                .execute(new Notificator(player))
                .submit(DailyRewards.getInst());
        taskMap.put(player.getUniqueId(), task);
    }
    public void removePlayer(Player player){
        if (taskMap.get(player.getUniqueId())==null){
            return;
        }
        taskMap.get(player.getUniqueId()).cancel();
        taskMap.remove(player.getUniqueId());
    }
}

class Notificator implements Runnable{

    private Player player;
    private int tryAmount;

    Notificator(Player player){
        ConfigurationNode node = DailyRewards.getInst().getRootDefNode();
        this.player=player;
        tryAmount=node.getNode("messages-amount").getInt(0);
    }

    @Override
    public void run() {
        if(DailyRewards.getInst().getSqlite().getStatus(player.getUniqueId())==1){
            DailyRewards.getInst().getNotificationManager().removePlayer(player);
            return;
        }
        player.sendMessage(trans("rewards-notification"));
        player.playSound(SoundTypes.ENTITY_EXPERIENCE_ORB_PICKUP, player.getLocation().getPosition(), 100);
        tryAmount--;
        if (tryAmount<=0){
            DailyRewards.getInst().getNotificationManager().removePlayer(player);
        }
    }
}