package ru.skyfire.zeta.dailyrewards.serializers;

import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import ru.skyfire.zeta.dailyrewards.DailyRewards;
import ru.skyfire.zeta.dailyrewards.Util;
import ru.skyfire.zeta.dailyrewards.reward.CmdReward;
import ru.skyfire.zeta.dailyrewards.reward.ItemReward;
import ru.skyfire.zeta.dailyrewards.reward.MoneyReward;
import ru.skyfire.zeta.dailyrewards.reward.Reward;

import java.math.BigDecimal;
import java.util.*;

import static ru.skyfire.zeta.dailyrewards.DailyRewards.logger;

public class RewardDeserializer {
    public Map<String, List<Reward>> rewardMap = new HashMap<>();
    public Map<String, ItemStack> iconMap = new LinkedHashMap<>();

    public RewardDeserializer(ConfigurationNode node) {
        Map<String, List<Reward>> bufRewardMap = new HashMap<>();
        if (node.getNode("days").getChildrenMap().keySet().isEmpty()) {
            logger.info("Alarm! Can't read rewards from config! Is it empty?");
            return;
        }
        for (Object n : node.getNode("days").getChildrenMap().keySet()) {
            List<Reward> list = new ArrayList<>();
            for (ConfigurationNode m : node.getNode("days").getNode(n.toString()).getNode("rewards").getChildrenList()) {
                String reward = m.getNode("reward").getString();
                String[] spl = reward.split(" ");
                switch (spl[0]) {
                    case "ITEM":
                        int amount = Integer.valueOf(spl[2]);
                        ItemStack stack = Util.parseItem(spl[1]);
                        list.add(new ItemReward(stack, amount));
                        if (DailyRewards.getInst().debug) {
                            logger.info("ITEM reward " + spl[1] + " " + spl[2] + " is finished.");
                        }
                        break;
                    case "MONEY":
                        BigDecimal money = new BigDecimal(spl[1]);
                        list.add(new MoneyReward(money));
                        if (DailyRewards.getInst().debug) {
                            logger.info("MONEY reward " + spl[1] + " is finished.");
                        }
                        break;
                    case "CMD":
                        String string = m.getNode("reward").getString();
                        String cmd = string.substring(string.indexOf(" ")).substring(1);
                        list.add(new CmdReward(cmd));
                        if (DailyRewards.getInst().debug) {
                            logger.info("CMD reward " + cmd + " is finished.");
                        }
                        break;
                    default:
                        logger.error("Alarm! Error in config! Fix it, please! "+reward);
                        return;
                }
            }
            if (DailyRewards.getInst().debug) {
                logger.info("Day " + n.toString() + " is finished.");
            }
            bufRewardMap.put(n.toString(), list);
        }

        rewardMap = bufRewardMap;

        Map<String, ItemStack> bufIconMap = new LinkedHashMap<>();
        ConfigurationNode days = DailyRewards.getInst().getRootDefNode().getNode("days");
        for (String a : rewardMap.keySet()){
            ItemStack stack = Util.parseItem(days.getNode(a, "icon", "item").getString("minecraft:diamond"));
            if (stack != null) {
                stack.offer(Keys.DISPLAY_NAME, Text.of(days.getNode(a, "icon", "name").getString("Awesame Day!").replace("&","§")));
                List<Text> lore = new ArrayList<>();
                for (Object b : days.getNode(a, "icon", "lore").getChildrenMap().keySet()){
                    lore.add(Text.of(days.getNode(a, "icon", "lore").getChildrenMap().get(b).getString().replace("&","§")));
                }
                stack.offer(Keys.ITEM_LORE, lore);
            }
            bufIconMap.put(a, stack != null ? stack.copy() : Util.parseItem("minecraft:dirt"));
        }

        iconMap = bufIconMap;

        if (rewardMap.isEmpty()){
            logger.error("Alarm! There are no rewards in memory! Check config!");
        }
        if(DailyRewards.getInst().debug){
            for(String a : iconMap.keySet()){
                logger.info("Icon loaded - item - "+iconMap.get(a).getType().getName());
            }
        }
    }
}
