package ru.skyfire.zeta.dailyrewards.commands;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import ru.skyfire.zeta.dailyrewards.util.GuiUtil;

import static ru.skyfire.zeta.dailyrewards.util.TextUtil.trans;

public class CmdDailyShow implements CommandExecutor {
    public CommandResult execute(CommandSource sender, CommandContext args) {
        if (!(sender instanceof Player)){
            sender.sendMessage(trans("command-playeronly"));
            return CommandResult.success();
        }
        GuiUtil.showRewards((Player) sender);
        return CommandResult.success();
    }
}
