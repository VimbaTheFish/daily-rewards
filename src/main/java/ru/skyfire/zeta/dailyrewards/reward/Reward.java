package ru.skyfire.zeta.dailyrewards.reward;

import org.spongepowered.api.entity.living.player.Player;

public abstract class Reward {
    public abstract boolean check(Player player);
    public abstract void apply(Player player);
}
