package ru.skyfire.zeta.dailyrewards.commands;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import ru.skyfire.zeta.dailyrewards.DailyRewards;
import ru.skyfire.zeta.dailyrewards.database.SqliteEntry;

import static ru.skyfire.zeta.dailyrewards.util.TextUtil.trans;

public class CmdDailyInfo implements CommandExecutor {
    public CommandResult execute(CommandSource sender, CommandContext args) {
        Player targetPlayer = args.<Player>getOne("player").orElse(null);
        if(targetPlayer==null){
            sender.sendMessage(trans("command-set-noplayer"));
            return CommandResult.success();
        }
        SqliteEntry sqlite = DailyRewards.getInst().getSqlite();
        sender.sendMessage(Text.of("Player's next day: "+sqlite.getCurrentDay(targetPlayer.getUniqueId())));
        if(sqlite.getStatus(targetPlayer.getUniqueId())==1){
            sender.sendMessage(Text.of("Reward is taken"));
        } else {
            sender.sendMessage(Text.of("Reward is NOT taken"));
        }
        return CommandResult.success();
    }
}
