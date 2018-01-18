package ru.skyfire.zeta.dailyrewards.commands;

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class CmdDailyHelp implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) {
        if (!sender.hasPermission("dailyrewards.admin") && sender instanceof Player) {
            Text.Builder t = Text.builder();
            t.append(Text.of(TextColors.AQUA, Text.of("Aloha, bro! That's DailyReward info page!")));
            t.append(Text.NEW_LINE);
            t.append(Text.of(TextColors.AQUA, Text.of("That's commands you need:")));
            t.append(Text.NEW_LINE);
            t.append(Text.of(TextColors.AQUA, Text.of("/dr take - take your reward! Executes on GUI button")));
            t.append(Text.NEW_LINE);
            t.append(Text.of(TextColors.AQUA, Text.of("/dr show - shows reward GUI again!")));
            t.append(Text.NEW_LINE);
            t.append(Text.of(TextColors.AQUA, Text.of("That's all! Good luck!")));
            sender.sendMessage(t.build());
            return CommandResult.success();
        }
        Text.Builder t = Text.builder();
        t.append(Text.of(TextColors.AQUA, Text.of("Aloha, bro! That's DailyReward info page!")));
        t.append(Text.NEW_LINE);
        t.append(Text.of(TextColors.AQUA, Text.of("You are admin, as I see. Cool.")));
        t.append(Text.NEW_LINE);
        t.append(Text.of(TextColors.AQUA, Text.of("/dr take - take your reward! Executes on GUI button")));
        t.append(Text.NEW_LINE);
        t.append(Text.of(TextColors.AQUA, Text.of("/dr show - shows reward GUI again!")));
        t.append(Text.NEW_LINE);
        t.append(Text.of(TextColors.AQUA, Text.of("/dr info <player> - shows info about player")));
        t.append(Text.NEW_LINE);
        t.append(Text.of(TextColors.AQUA, Text.of("/dr set day <day> <player> - set typed day to player")));
        t.append(Text.NEW_LINE);
        t.append(Text.of(TextColors.AQUA, Text.of("/dr set status 1/0 <player> - " +
                "sets status to player (1 - reward is taken, 0 - reward is not taken)")));
        t.append(Text.NEW_LINE);
        t.append(Text.of(TextColors.AQUA, Text.of("/dr reload - summons kitties")));
        t.append(Text.NEW_LINE);
        t.append(Text.of(TextColors.AQUA, Text.of("That's all! Good luck!")));
        sender.sendMessage(t.build());
        return CommandResult.success();
    }
}
