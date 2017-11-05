package ru.skyfire.zeta.dailyrewards.commands;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import ru.skyfire.zeta.dailyrewards.DailyRewards;

public class CmdDailyClear implements CommandExecutor {
    public CommandResult execute(CommandSource sender, CommandContext args) {
        DailyRewards.getInst().getSqlite().clearStatuses();
        sender.sendMessage(Text.of("Настал новый день, братья!"));
        return CommandResult.success();
    }
}
