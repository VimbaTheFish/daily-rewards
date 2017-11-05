package ru.skyfire.zeta.dailyrewards;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import ru.skyfire.zeta.dailyrewards.database.SqliteEntry;

import java.util.Optional;

import static ru.skyfire.zeta.dailyrewards.DailyRewards.logger;

public class Listeners {
    @Listener
    public void onPlayerConnect(ClientConnectionEvent.Join event){
        SqliteEntry sqlite = DailyRewards.getInst().getSqlite();
        Cause cause = event.getCause(); // [Player, Entity]
        Optional<Player> firstPlayer = cause.first(Player.class);
        if (!firstPlayer.isPresent()){
            logger.error("Player error - cannot find player");
            return;
        }
        Player player = firstPlayer.get();
        if (sqlite.getStatus(player.getUniqueId())<0){
            sqlite.addEntry(player.getUniqueId(), 1, 0);
        }

    }
}
