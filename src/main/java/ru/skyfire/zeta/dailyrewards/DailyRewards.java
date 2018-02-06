package ru.skyfire.zeta.dailyrewards;

import com.google.inject.Inject;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import ru.skyfire.zeta.dailyrewards.commands.CmdBuilder;
import ru.skyfire.zeta.dailyrewards.database.SqliteEntry;
import ru.skyfire.zeta.dailyrewards.listeners.Listeners;
import ru.skyfire.zeta.dailyrewards.notification.NotificationManager;
import ru.skyfire.zeta.dailyrewards.serializers.RewardDeserializer;

import java.io.IOException;
import java.nio.file.Path;

@Plugin(id = "dailyrewards", name = "DailyRewards", description = "Plugin for rewarding players for every day logging it!")
public class DailyRewards {
    private ConfigurationNode rootDefNode;
    private ConfigurationNode rootTranslationNode;
    private ConfigurationNode rootTimeNode;
    private static DailyRewards inst;
    public static Logger logger;
    public boolean debug=false;
    public boolean hardMode=false;
    private SqliteEntry sqlite;
    private RewardDeserializer rewardDeserializer;
    private NotificationManager notificationManager;

    @Inject
    private PluginContainer plugin;
    @Inject
    private void setLogger(Logger logger){
        this.logger = logger;
    }

    @Inject
    @DefaultConfig(sharedRoot = false)
    private ConfigurationLoader<CommentedConfigurationNode> configLoader;
    @Inject
    @ConfigDir(sharedRoot = false)
    private Path privateConfigDir;
    @Inject
    @DefaultConfig(sharedRoot = false)
    private Path defaultConfig;

    private ConfigurationLoader<CommentedConfigurationNode> timeConfigLoader;

    public static DailyRewards getInst() {
        return inst;
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event){
        inst = this;
        initConfigBase();
        sqlite = new SqliteEntry();
        rewardDeserializer = new RewardDeserializer(rootDefNode);
        notificationManager = new NotificationManager();
        initCommands();
        initTranslaitionConfig();
        initTimeConfig();
        Sponge.getEventManager().registerListeners(this, new Listeners());
    }

    @Listener
    public void onServerClose(GameStoppingServerEvent event){
        logger.info("DailyRewards was unloaded!");
    }

    public void initConfigBase() {
        if (!defaultConfig.toFile().exists()) {
            try {
                plugin.getAsset("DailyRewards.conf").orElse(null).copyToFile(defaultConfig);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            rootDefNode = configLoader.load();
            debug=rootDefNode.getNode("debug").getBoolean();
            hardMode=rootDefNode.getNode("hardMode").getBoolean();
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
    }

    public void initTranslaitionConfig() {
        Path translationPath = privateConfigDir.resolve("messages.conf");
        if (!translationPath.toFile().exists()) {
            logger.error("Cannot find translation file path");
            try {
                plugin.getAsset("messages.conf").orElse(null).copyToDirectory(privateConfigDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            rootTranslationNode = HoconConfigurationLoader.builder().setPath(translationPath).build().load();
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
    }

    public void initTimeConfig() {
        Path timePath = privateConfigDir.resolve("time.conf");
        ConfigurationLoader<CommentedConfigurationNode> timeCL = HoconConfigurationLoader.builder().setPath(timePath).build();

        if (!timePath.toFile().exists()) {
            logger.error("Cannot find time file path");
            try {
                plugin.getAsset("time.conf").orElse(null).copyToDirectory(privateConfigDir);
                CommentedConfigurationNode node = timeCL.load();
                node.getNode("time").setValue(System.currentTimeMillis());
                timeCL.save(node);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            rootTimeNode = timeCL.load();
            timeConfigLoader = timeCL;
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
    }

    public PluginContainer getPlugin() {
        return plugin;
    }

    private void initCommands(){
        new CmdBuilder(plugin);
    }

    public Path getConfigDir() {
        return privateConfigDir;
    }

    public SqliteEntry getSqlite() {
        return sqlite;
    }

    public ConfigurationNode getRootDefNode() {
        return rootDefNode;
    }

    public ConfigurationNode getRootTranslationNode() {
        return rootTranslationNode;
    }

    public ConfigurationNode getRootTimeNode() {
        return rootTimeNode;
    }

    public void setRootTimeNode(ConfigurationNode rootTimeNode) {
        this.rootTimeNode = rootTimeNode;
    }

    public RewardDeserializer getRewardDeserializer() {
        return rewardDeserializer;
    }

    public void setRewardDeserializer(RewardDeserializer rewardDeserializer) {
        this.rewardDeserializer = rewardDeserializer;
    }

    public ConfigurationLoader<CommentedConfigurationNode> getTimeConfigLoader() {
        return timeConfigLoader;
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }
}
