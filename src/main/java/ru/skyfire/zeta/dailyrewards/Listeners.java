package ru.skyfire.zeta.dailyrewards;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import ru.skyfire.zeta.dailyrewards.reward.Reward;
import ru.skyfire.zeta.dailyrewards.reward.StubReward;

import java.util.ArrayList;
import java.util.List;

public class Listeners {
    @Listener
    public void onPlayerConnect(ClientConnectionEvent.Join event, @First Player player){
        List<Reward> rewards = new ArrayList<>();
        rewards.add(new StubReward(ItemStack.of(ItemTypes.DIAMOND, 1), "gsfas", true));
        rewards.add(new StubReward(ItemStack.of(ItemTypes.COAL, 1), "gsfas1", false));
        rewards.add(new StubReward(ItemStack.of(ItemTypes.HAY_BLOCK, 1), "gsfas2", false));
        rewards.add(new StubReward(ItemStack.of(ItemTypes.VINE, 1), "gsfa3s", false));
        rewards.add(new StubReward(ItemStack.of(ItemTypes.QUARTZ, 1), "gsfas4", false));
        rewards.add(new StubReward(ItemStack.of(ItemTypes.WHEAT, 1), "gsf56as", false));
        rewards.add(new StubReward(ItemStack.of(ItemTypes.SADDLE, 1), "sj", false));
        rewards.add(new StubReward(ItemStack.of(ItemTypes.HOPPER, 1), "gdf", false));


        Util.showRewards(player, rewards);
//        SqliteEntry sqlite = DailyRewards.getInst().getSqlite();
//        if (sqlite.getStatus(player.getUniqueId())<0){
//            sqlite.addEntry(player.getUniqueId(), 1, 0);
//        }

    }
}
