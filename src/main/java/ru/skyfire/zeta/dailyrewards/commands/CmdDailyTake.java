package ru.skyfire.zeta.dailyrewards.commands;

import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import ru.skyfire.zeta.dailyrewards.DailyRewards;
import ru.skyfire.zeta.dailyrewards.Util;
import ru.skyfire.zeta.dailyrewards.database.SqliteEntry;
import ru.skyfire.zeta.dailyrewards.reward.Reward;

import java.io.IOException;
import java.util.List;

public class CmdDailyTake implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Text.of(Util.trans("command-playeronly")));
            return CommandResult.success();
        }
        Player player = (Player) sender;

        SqliteEntry sqlite = DailyRewards.getInst().getSqlite();
        ConfigurationNode node = DailyRewards.getInst().getRootDefNode();

        timeCheck();

        if (sqlite.getCurrentDay(player.getUniqueId())<1){
            sender.sendMessage(Text.of(Util.trans("command-take-first")));
            sqlite.addEntry(player.getUniqueId(), 1, 0);
        }

        int currDay = sqlite.getCurrentDay(player.getUniqueId());
        ConfigurationNode currentDay = node.getNode("days", String.valueOf(currDay));
        List<Reward> rewards = DailyRewards.getInst().getRewardDeserializer().rewardMap.get(String.valueOf(currDay));

        if(sqlite.getStatus(player.getUniqueId())==1){
            sender.sendMessage(Text.of(Util.trans("command-take-taken")));
            return  CommandResult.success();
        }

        if (rewards==null){
            sender.sendMessage(Text.of(Util.trans("command-take-noreward")));
        } else {
            if(!giveReward(player, currDay)){
                player.sendMessage(Text.of(Util.trans("command-take-fail")));
                return CommandResult.success();
            }
            sender.sendMessage(Text.of(Util.trans("command-take-reward")));
        }

        if(currDay>=node.getNode("daycap").getInt()){
            sqlite.updateEntry(player.getUniqueId(), 1, 1);
        } else {
            sqlite.updateEntry(player.getUniqueId(), currDay+1, 1);
        }
        sender.sendMessage(Text.of(Util.trans("command-take-currentday")+" "+currDay));

        return CommandResult.success();
    }

    private boolean giveReward(Player player, int currDay){
        List<Reward> rewards = DailyRewards.getInst().getRewardDeserializer().rewardMap.get(String.valueOf(currDay));
        for(Reward a : rewards){
            if(!a.check(player)){
                return false;
            }
        }
        for(Reward a : rewards){
            a.apply(player);
        }
        return true;
    }

    private void timeCheck(){
        ConfigurationNode timeNode = DailyRewards.getInst().getRootTimeNode();
        double lastDayTime = timeNode.getNode("time").getDouble();
        double currentTime = System.currentTimeMillis();
        double oneDay = 24*60*60*1000;

        if(lastDayTime<currentTime-oneDay){
            int lostDays=0;
            while (currentTime-oneDay>lastDayTime){
                lostDays++;
                lastDayTime=lastDayTime+oneDay;
            }
            timeNode.getNode("time").setValue(lastDayTime);
            try {
                DailyRewards.getInst().getTimeConfigLoader().save(timeNode);
            } catch (IOException e) {
                e.printStackTrace();
            }
            DailyRewards.getInst().getSqlite().clearStatuses();
            if (DailyRewards.getInst().hardMode){
                DailyRewards.getInst().getSqlite().clearDaysHard();
            }
        }
    }
}
