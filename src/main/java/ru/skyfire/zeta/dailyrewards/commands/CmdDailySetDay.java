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

public class CmdDailySetDay implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) {
        Player targetPlayer = args.<Player>getOne("player").orElse(null);
        Integer targetAmount = args.<Integer>getOne("amount").orElse(null);

        if (targetAmount==null || targetPlayer==null){
            sender.sendMessage(trans("command-set-error"));
            return CommandResult.success();
        }

        SqliteEntry sqlite = DailyRewards.getInst().getSqlite();

        if(sqlite.getStatus(targetPlayer.getUniqueId())==-1){
            sender.sendMessage(trans("command-set-noplayer"));
            return CommandResult.success();
        }

        sqlite.updateEntry(targetPlayer.getUniqueId(), targetAmount, sqlite.getStatus(targetPlayer.getUniqueId()));
        sender.sendMessage(trans("command-set-day-success")
                .toBuilder()
                .append(Text.of(" "+targetAmount))
                .build());
        return CommandResult.success();
    }
}
