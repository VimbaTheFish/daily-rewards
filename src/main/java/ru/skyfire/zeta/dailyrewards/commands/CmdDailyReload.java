package ru.skyfire.zeta.dailyrewards.commands;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import ru.skyfire.zeta.dailyrewards.DailyRewards;
import ru.skyfire.zeta.dailyrewards.Util;

public class CmdDailyReload implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) {
        DailyRewards.getInst().initConfigBase();
        sender.sendMessage(Text.of(Util.trans("command-reload-success")));
        return CommandResult.success();
    }
}
