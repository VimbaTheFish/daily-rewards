package ru.skyfire.zeta.dailyrewards.reward;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

public class StubReward extends Reward {
    private boolean isAvailable = false;

    public StubReward(ItemStack icon, String name, boolean isAvailable) {
        this.icon = icon;
        this.name = name;
        this.isAvailable = isAvailable;
    }

    @Override
    public boolean check(Player player) {
        return isAvailable;
    }

    @Override
    public void apply(Player player) {
        player.sendMessage(Text.of("This is your " + name));
    }

    @Override
    public String toString() {
        return "StubReward{" +
                "isAvailable=" + isAvailable +
                ", icon=" + icon +
                ", name='" + name + '\'' +
                '}';
    }
}
