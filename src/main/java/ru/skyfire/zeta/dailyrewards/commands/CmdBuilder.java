package ru.skyfire.zeta.dailyrewards.commands;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import ru.skyfire.zeta.dailyrewards.commands.*;

public class CmdBuilder {
    public CmdBuilder(PluginContainer plugin){
        CommandSpec dailyHelp = CommandSpec.builder()
                .permission("dailyrewards.help")
                .description(Text.of("Daily Rewards help command!"))
                .executor(new CmdDailyHelp())
                .build();
        CommandSpec dailySetDay = CommandSpec.builder()
                .permission("dailyrewards.admin")
                .description(Text.of("Set current day for player"))
                .executor(new CmdDailySetDay())
                .build();
        CommandSpec dailySetStatus = CommandSpec.builder()
                .permission("dailyrewards.admin")
                .description(Text.of("Set current status for player"))
                .executor(new CmdDailySetStatus())
                .build();
        CommandSpec dailyTake = CommandSpec.builder()
                .permission("dailyrewards.base")
                .description(Text.of("Command to take daily reward"))
                .executor(new CmdDailyTake())
                .build();
        CommandSpec dailyClear = CommandSpec.builder()
                .permission("dailyrewards.admin")
                .description(Text.of("Command to clear daily rewards"))
                .executor(new CmdDailyClear())
                .build();
        CommandSpec dailyShow = CommandSpec.builder()
                .permission("dailyrewards.base")
                .description(Text.of("Command to show daily rewards"))
                .executor(new CmdDailyShow())
                .build();
        CommandSpec dailyReload = CommandSpec.builder()
                .permission("dailyrewards.admin")
                .description(Text.of("Command to reload daily rewards"))
                .executor(new CmdDailyReload())
                .build();

        CommandSpec dailySet = CommandSpec.builder()
                .permission("dailyrewards.admin")
                .description(Text.of("Command-setter"))
                .executor(new CmdDailySet())
                .child(dailySetDay, "day")
                .child(dailySetStatus, "status")
                .build();

        CommandSpec daily = CommandSpec.builder()
                .permission("dailyrewards.help")
                .description(Text.of("Main command"))
                .executor(new CmdDaily())
                .child(dailyHelp, "help")
                .child(dailySet, "set")
                .child(dailyTake, "take")
                .child(dailyClear, "clear")
                .child(dailyShow, "show")
                .child(dailyReload, "reload")
                .build();

        Sponge.getCommandManager().register(plugin, daily, "dailyrewards", "daily", "dr");
    }
}
