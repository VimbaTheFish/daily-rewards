package ru.skyfire.zeta.dailyrewards.listeners;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import ru.skyfire.zeta.dailyrewards.DailyRewards;
import ru.skyfire.zeta.dailyrewards.database.SqliteEntry;
import ru.skyfire.zeta.dailyrewards.util.GuiUtil;

import java.util.concurrent.TimeUnit;

import static ru.skyfire.zeta.dailyrewards.DailyRewards.logger;

public class Listeners {
    @Listener
    public void onServerStart(GameStartedServerEvent event){
        logger.info("DailyRewards is loaded!");
    }

    @Listener
    public void onPlayerConnect(ClientConnectionEvent.Join event, @First Player player){

        SqliteEntry sqlite = DailyRewards.getInst().getSqlite();
        if(!player.hasPermission("dailyrewards.base")){
            return;
        }
        if (sqlite.getStatus(player.getUniqueId())==-1){
            sqlite.addEntry(player.getUniqueId(), 1, 0);
        }
        if (sqlite.getStatus(player.getUniqueId())==1){
            return;
        }
        if (DailyRewards.getInst().getRootDefNode().getNode("show-rewards-on-join").getBoolean(true)){
            if (sqlite.getStatus(player.getUniqueId())==0 && player.hasPermission("dailyrewards.base")){
                int delay = DailyRewards.getInst().getRootDefNode().getNode("guiShowDelay").getInt(0);
                Sponge.getScheduler().createTaskBuilder()
                        .delay(delay, TimeUnit.SECONDS)
                        .execute(t->{
                            GuiUtil.showRewards(player);
                            t.cancel();
                        })
                        .submit(DailyRewards.getInst());
            }
        }

        if(DailyRewards.getInst().getRootDefNode().getNode("messages-amount").getInt(0)>0){
            DailyRewards.getInst().getNotificationManager().addPlayer(player);
            if (DailyRewards.getInst().debug){
                logger.info("Notification for player "+player.getName()+" started");
            }
        }
    }

    @Listener
    public void onPlayerExit(ClientConnectionEvent.Disconnect event, @First Player player){
        DailyRewards.getInst().getNotificationManager().removePlayer(player);
    }

    @Listener
    public void onServerClose(GameStoppingServerEvent event){
        logger.info("DailyRewards was unloaded!");
    }

}
