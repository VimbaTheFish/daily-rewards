package ru.skyfire.zeta.dailyrewards.util;

import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetype;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.property.InventoryCapacity;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.item.inventory.transaction.SlotTransaction;
import org.spongepowered.api.text.Text;
import ru.skyfire.zeta.dailyrewards.DailyRewards;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.skyfire.zeta.dailyrewards.util.ItemUtil.isItemStacksSimilar;
import static ru.skyfire.zeta.dailyrewards.util.ItemUtil.parseItem;
import static ru.skyfire.zeta.dailyrewards.util.TextUtil.trans;

public class GuiUtil {
    public static void showRewards(final Player player) {
        final Map<String, ItemStack> iconMap = DailyRewards.getInst().getRewardDeserializer().iconMap;
        ConfigurationNode rootNode = DailyRewards.getInst().getRootDefNode();
        int inventorySize = (int) (Math.ceil(rootNode.getNode("daycap").getInt()/7.0) * 9);

        ItemStack stub = ItemStack.of(ItemTypes.GLASS_PANE, 1);
        stub.offer(Keys.DYE_COLOR, DyeColors.GREEN);
        stub.offer(Keys.DISPLAY_NAME, Text.of(trans("rewards-inventory-stub")));
        ItemStack button = parseItem(rootNode.getNode("button").getString());

        InventoryArchetype archetype = InventoryArchetype.builder()
                .property(InventoryCapacity.of(inventorySize))
                .build("daily-inv", "rewards-inventory-name");
        Inventory inventory = Inventory.builder()
                .of(archetype)
                .property(InventoryTitle.PROPERTY_NAME, InventoryTitle.of(trans("rewards-inventory-name")))
                .listener(
                        ClickInventoryEvent.class,
                        e -> {
                            e.setCancelled(true);
                            Player p = e.getCause().first(Player.class).orElse(null);
                            List<SlotTransaction> transactions = e.getTransactions();
                            Set<String> keys = iconMap.keySet();
                            List<String> applicableKeys = keys.stream()
                                    .filter(i -> transactions.stream().anyMatch(t -> isItemStacksSimilar(button, t.getOriginal().createStack())))
                                    .collect(Collectors.toList());
                            if (!applicableKeys.isEmpty()) {
                                Sponge.getScheduler()
                                        .createTaskBuilder()
                                        .delayTicks(2)
                                        .execute(r -> Sponge.getCommandManager().process(player, "dr take"))
                                        .submit(DailyRewards.getInst());
                                Sponge.getScheduler()
                                        .createTaskBuilder()
                                        .delayTicks(1)
                                        .execute(r -> player.closeInventory())
                                        .submit(DailyRewards.getInst());
                            }
                        }
                )
                .build(DailyRewards.getInst());

        Iterator inventoryIt = inventory.slots().iterator();
        int i = 1;
        int j = 1;
        Slot slot = null;
        while (inventoryIt.hasNext()) {
            slot = (Slot) inventoryIt.next();
            if (i % 9 == 0 || (i - 1) % 9 == 0) {
                slot.offer(stub.copy());
            } else {
                if (iconMap.get(String.valueOf(j)) == null) {
                    j++;
                } else {
                    ItemStack is = iconMap.get(String.valueOf(j));
                    is.setQuantity(j);
                    slot.offer(is);
                    j++;
                }
            }
            i++;
        }
        if(slot!=null){
            ItemStack stack = parseItem("minecraft:end_crystal");
            stack.offer(Keys.DISPLAY_NAME, Text.of(trans("rewards-inventory-take")));
            slot.poll();
            slot.offer(stack.copy());
        }

        player.openInventory(inventory);
    }
}
