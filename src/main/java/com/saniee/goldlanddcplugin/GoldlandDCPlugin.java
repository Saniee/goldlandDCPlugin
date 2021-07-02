package com.saniee.goldlanddcplugin;

import com.saniee.goldlanddcplugin.dcCommands.info;
import com.saniee.goldlanddcplugin.dcCommands.lookUp;
import com.saniee.goldlanddcplugin.mcCommands.sync;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;

/* TODO
* Remove the commands that are not used.
*/

public final class GoldlandDCPlugin extends JavaPlugin {

    JDA bot = null;

    @Override
    public void onEnable() {
        // Plugin startup logic

        getConfig().options().copyDefaults();
        saveDefaultConfig();
        if (!mainDependEnabled()) {
            getLogger().info("Some Dependencies not found, disabling! Please check if you have these installed: Vault, Essentials, AdvancedBan, CrazyAuctions");
            getServer().getPluginManager().disablePlugin(this);
        } else {
            getLogger().info("Starting!");

            try {
                JDABuilder jdaBuilder = JDABuilder.createDefault(getConfig().getString("Token"));
                bot = jdaBuilder.build().awaitReady();
                getLogger().info("DC Bot Started!");
            } catch (LoginException | InterruptedException e) {
                e.printStackTrace();
            }

            bot.addEventListener(new info(this, bot), new lookUp(this, bot), new ChatToMC(this, bot));
            this.getServer().getPluginCommand("sync").setExecutor(new sync(this, bot));
            this.getServer().getPluginManager().registerEvents(new ChatToDC(this, bot), this);
            this.getServer().getPluginManager().registerEvents(new BansToDC(this, bot), this);
            this.getServer().getPluginManager().registerEvents(new AhToDC(this, bot), this);

            getLogger().info("Plugin Started!");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Plugin Stopped!");
    }

    private boolean mainDependEnabled() {
        if (getServer().getPluginManager().getPlugin("Essentials") == null || getServer().getPluginManager().getPlugin("Vault") == null || getServer().getPluginManager().getPlugin("AdvancedBan") == null || getServer().getPluginManager().getPlugin("CrazyAuctions") == null) {
            return false;
        } else {
            return true;
        }
    }
}
