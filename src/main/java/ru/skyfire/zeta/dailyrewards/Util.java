package ru.skyfire.zeta.dailyrewards;

import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;

import java.util.Optional;

import static ru.skyfire.zeta.dailyrewards.DailyRewards.logger;

public class Util {
    public static int giveItemToInventory(Inventory inv, ItemStack itemStack, int amount) {
        int bufamount = amount;
        for (Inventory s : inv.slots()) {
            Slot slot = (Slot) s;
            ItemStack slotStack = slot.peek().orElse(null);
            if (slotStack == null) {
                if (bufamount > itemStack.getMaxStackQuantity()) {
                    ItemStack newitem = itemStack.copy();
                    newitem.setQuantity(newitem.getMaxStackQuantity());
                    slot.offer(newitem);
                    bufamount = bufamount - itemStack.getMaxStackQuantity();
                    continue;
                } else {
                    ItemStack newitem = itemStack.copy();
                    newitem.setQuantity(bufamount);
                    slot.offer(newitem);
                    bufamount = 0;
                    break;
                }
            }
            int itemMeta = (int) itemStack.toContainer().get(DataQuery.of("UnsafeDamage")).orElse(0);
            int slotMeta = (int) slotStack.toContainer().get(DataQuery.of("UnsafeDamage")).orElse(0);
            if (!(itemStack.getType() == slotStack.getType() && itemMeta==slotMeta)) {
                continue;
            }

            int add = slotStack.getMaxStackQuantity() - slotStack.getQuantity();
            if (bufamount > add) {
                bufamount = bufamount - add;
                slotStack.setQuantity(slotStack.getMaxStackQuantity());
                slot.poll();
                slot.offer(slotStack);
            } else {
                int bufquant = slotStack.getQuantity() + bufamount;
                slotStack.setQuantity(bufquant);
                slot.poll();
                slot.offer(slotStack);
                break;
            }
        }
        return bufamount;
    }


    public static int getFreeSlots(Inventory inv) {
        int res = 0;
        for (Inventory s : inv.slots()) {
            Slot slot = (Slot) s;
            if (!slot.peek().isPresent()) {
                res = res + 1;
            }
        }
        return res;
    }

    public static ItemStack parseItem(String text) {
        String[] parts = text.split(":");
        Optional<ItemType> itemType;
        int metadata = 0;
        switch (parts.length) {
            case 1:
                itemType = Sponge.getGame().getRegistry().getType(ItemType.class, text);
                break;
            case 2:
                itemType = Sponge.getGame().getRegistry().getType(ItemType.class, text);
                if (!itemType.isPresent()) {
                    itemType = Sponge.getRegistry().getType(ItemType.class, parts[0]);
                    try {
                        metadata = Integer.parseInt(parts[1]);
                    } catch (NumberFormatException exc) {
                        logger.warn("Metadata parsing failed: " + parts[1]);
                    }
                }
                break;
            case 3:
                itemType = Sponge.getRegistry().getType(ItemType.class, parts[0] + ":" + parts[1]);
                if (itemType.isPresent()) {
                    metadata = Integer.parseInt(parts[2]);
                } else {
                    logger.info("Problem with itemType config 3 - " + itemType.toString());
                }
                break;
            default:
                logger.warn("Too many colons");
                return null;
        }
        if (itemType.isPresent()) {
            ItemStack stack = ItemStack.of(itemType.get(), 1);
            DataContainer container = stack.toContainer();
            container.set(DataQuery.of("UnsafeDamage"), metadata);
            stack = ItemStack.builder().fromContainer(container).build();
            return stack;
        } else {
            logger.info("Problem with itemType config final - " + itemType.toString());
            return null;
        }
    }

    public static String cmdParser(String text, Player player){
        StringBuilder cmd = new StringBuilder();
        String[] parts = text.split(" ");
        for (String part : parts) {
            if (part.startsWith("<") && part.endsWith(">")) {
                switch (part) {
                    case "<player>":
                        part = part.replace(part, player.getName());
                }
            }
            cmd.append(" ").append(part);
        }
        return cmd.substring(1);
    }

    public static String trans(String nodeName){
        ConfigurationNode rootNode = DailyRewards.getInst().getRootTranslationNode();
        if (rootNode==null){
            logger.error("Alarm! Problems with config!");
            return "Problems with translation config!";
        }
        ConfigurationNode node = rootNode.getNode(nodeName);
        if (node.getString().isEmpty()){
            logger.error("Alarm! Cannot read translation file! Check: "+nodeName);
            return "§Translation error!";
        }
        return node.getString().replace("&","§");
    }
}
