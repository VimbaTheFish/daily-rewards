package ru.skyfire.zeta.dailyrewards.reward;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;

import static ru.skyfire.zeta.dailyrewards.util.ItemUtil.getFreeSlotsPlayer;
import static ru.skyfire.zeta.dailyrewards.util.ItemUtil.giveItemToPlayer;

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
        return getFreeSlotsPlayer(player);
    }

    @Override
    public boolean check(Player player) {
        int needempty = amount/getItemStack().getMaxStackQuantity();
        return needempty <= count(player);
    }

    @Override
    public void apply(Player player) {
        giveItemToPlayer(player, itemStack, amount);
    }
}
