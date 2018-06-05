package ru.skyfire.zeta.dailyrewards.reward;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import ru.skyfire.zeta.dailyrewards.DailyRewards;

import java.math.BigDecimal;
import java.util.Optional;

public class MoneyReward extends Reward {
    private BigDecimal amount;

    public MoneyReward(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean check(Player player) {
        Optional<EconomyService> serviceOpt = Sponge.getServiceManager().provide(EconomyService.class);
        if (!serviceOpt.isPresent()) {
            player.sendMessage(Text.of("Eco doesn't work"));
            return false;
        }
        EconomyService eco = serviceOpt.get();
        Currency curr = eco.getDefaultCurrency();
        UniqueAccount account = eco.getOrCreateAccount(player.getUniqueId()).get();
        if (account.hasBalance(curr)){
            return true;
        }
        return false;
    }

    @Override
    public void apply(Player player) {
        Optional<EconomyService> serviceOpt = Sponge.getServiceManager().provide(EconomyService.class);
        if (!serviceOpt.isPresent()) {
            player.sendMessage(Text.of("Eco doesn't work"));
            return;
        }
        EconomyService eco = serviceOpt.get();
        BigDecimal money = this.amount;
        Currency curr = eco.getDefaultCurrency();
        UniqueAccount account = eco.getOrCreateAccount(player.getUniqueId()).get();
        account.deposit(curr, money, Cause.of(EventContext.builder().build(), DailyRewards.getInst()));
        player.sendMessage(Text.of(TextColors.AQUA, Text.of("Your received money: "+getAmount())));
    }
}
