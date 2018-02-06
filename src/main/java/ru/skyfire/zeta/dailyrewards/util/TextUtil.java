package ru.skyfire.zeta.dailyrewards.util;

import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;
import ru.skyfire.zeta.dailyrewards.DailyRewards;

import static ru.skyfire.zeta.dailyrewards.DailyRewards.logger;

public class TextUtil {

    public static String cmdParser(String text, Player player){
        String cmd;
        cmd=text.replace("<player>", player.getName());
        return cmd;
    }

    public static Text colorString(String rawInput) {
        return TextSerializers.FORMATTING_CODE.deserialize(rawInput);
    }

    public static Text trans(String nodeName){
        ConfigurationNode rootNode = DailyRewards.getInst().getRootTranslationNode();
        if (rootNode==null){
            logger.error("Alarm! Problems with config!");
            return Text.of("Problems with translation config!");
        }
        ConfigurationNode node = rootNode.getNode(nodeName);
        if (node.getString()==null){
            logger.error("Alarm! Cannot read translation file! Check node: "+nodeName);
            return Text.of("§4Translation error! Check node: "+nodeName);
        }
        return colorString(node.getString());
    }
}
