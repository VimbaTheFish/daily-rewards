package ru.skyfire.zeta.dailyrewards.commands;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class CmdDaily implements CommandExecutor{
    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) {
        Text.Builder t = Text.builder();
        t.append(Text.of(TextColors.AQUA, Text.of("MUSHROOM DANCE,")));
        t.append(Text.NEW_LINE);
        t.append(Text.of(TextColors.AQUA, Text.of("MUSHROOM DANCE,")));
        t.append(Text.NEW_LINE);
        t.append(Text.of(TextColors.AQUA, Text.of("WHAT EVER COULD IT MEAN?")));
        t.append(Text.NEW_LINE);
        t.append(Text.of(TextColors.AQUA, Text.of("IT MEANS USE /dr help FOR SOME HELP!")));
        t.append(Text.NEW_LINE);
        t.append(Text.of(TextColors.GOLD, Text.of("DailyRewards")));
        sender.sendMessage(t.build());
        return CommandResult.success();
    }
}
