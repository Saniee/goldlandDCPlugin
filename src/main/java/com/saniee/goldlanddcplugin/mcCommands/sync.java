package com.saniee.goldlanddcplugin.mcCommands;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.UserDoesNotExistException;
import com.saniee.goldlanddcplugin.GoldlandDCPlugin;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.awt.*;
import java.math.BigDecimal;
import java.time.Instant;

public class sync implements CommandExecutor {
    private final GoldlandDCPlugin plugin;
    JDA Bot;
    TextChannel channel;

    public sync(GoldlandDCPlugin plugin, JDA Bot) {
        this.Bot = Bot;
        this.plugin = plugin;

        String id = plugin.getConfig().getString("SyncChatId");
        channel = Bot.getTextChannelById(id);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            BigDecimal money = null;
            Object kills = null;
            Object deaths = null;
            Object quit = null;
            try {
                money = Economy.getMoneyExact(player.getUniqueId());
                kills = player.getStatistic(Statistic.PLAYER_KILLS);
                deaths = player.getStatistic(Statistic.DEATHS);
                quit = player.getStatistic(Statistic.LEAVE_GAME);
            } catch (UserDoesNotExistException e) {
                e.printStackTrace();
            }
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERPEARL_THROW, 100, 0);
            player.sendMessage(ChatColor.GREEN + "Money: " + ChatColor.GOLD + money);
            player.sendMessage(ChatColor.GREEN + "Kills: " + ChatColor.RED + kills);
            player.sendMessage(ChatColor.GREEN + "Deaths: " + ChatColor.RED + deaths);
            player.sendMessage(ChatColor.GREEN + "Left the server: " + ChatColor.AQUA + quit + " times.");
            player.sendMessage(ChatColor.GREEN + "Data Sent to Discord!");
            plugin.getLogger().info(player.getName() + "'s data sent to Discord!");

            EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("Info About: " + player.getName());
                eb.setColor(Color.YELLOW);
                eb.addField("Money", String.valueOf(money), false);
                eb.addField("Kills", String.valueOf(kills), false);
                eb.addField("Deaths", String.valueOf(deaths), false);
                eb.addField("Quit", quit + " times.", false);
                eb.setFooter("Requested By: " + player.getName() + "! From the Server!");
                eb.setTimestamp(Instant.now());
            channel.sendMessage(eb.build()).queue();
        } else {
            plugin.getLogger().info("Not a Console Command!");
        }
        return true;
    }
}
