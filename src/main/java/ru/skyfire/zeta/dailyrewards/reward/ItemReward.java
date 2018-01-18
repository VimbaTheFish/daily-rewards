package ru.skyfire.zeta.dailyrewards.reward;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.entity.PlayerInventory;
import ru.skyfire.zeta.dailyrewards.Util;
import ru.skyfire.zeta.dailyrewards.reward.Reward;

public class ItemReward extends Reward {
    ItemStack itemStack;
    int amount;

    public ItemReward(ItemStack itemStack, int amount) {
        this.itemStack = itemStack;
        this.amount = amount;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    private int count(Player player){
        return Util.getFreeSlotsPlayer(player);
    }

    @Override
    public boolean check(Player player) {
        int needempty = amount/getItemStack().getMaxStackQuantity();
        return needempty <= count(player);
    }

    @Override
    public void apply(Player player) {
        Util.giveItemToPlayer(player, itemStack, amount);
    }
}
