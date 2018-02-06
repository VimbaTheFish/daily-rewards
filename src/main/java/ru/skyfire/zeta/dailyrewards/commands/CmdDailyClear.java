package ru.skyfire.zeta.dailyrewards.commands;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import ru.skyfire.zeta.dailyrewards.DailyRewards;
import ru.skyfire.zeta.dailyrewards.util.CmdUtil;
import ru.skyfire.zeta.dailyrewards.util.TimeUtil;

public class CmdDailyClear implements CommandExecutor {
    public CommandResult execute(CommandSource sender, CommandContext args) {
        CmdUtil.setNewDay();

        sender.sendMessage(Text.of("Every player now can take reward again! Time set to current!"));
        return CommandResult.success();
    }
}
