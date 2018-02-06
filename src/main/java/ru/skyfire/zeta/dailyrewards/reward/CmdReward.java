package ru.skyfire.zeta.dailyrewards.reward;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import static ru.skyfire.zeta.dailyrewards.util.TextUtil.cmdParser;

public class CmdReward extends Reward {
    String cmd;

    public CmdReward(String cmd) {
        this.cmd = cmd;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    @Override
    public boolean check(Player player) {
        return true;
    }

    @Override
    public void apply(Player player) {
        Sponge.getCommandManager().process(Sponge.getServer().getConsole(), cmdParser(this.cmd, player));
    }
}
