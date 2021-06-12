package com.saniee.goldlanddcplugin.dcCommands;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.UserDoesNotExistException;
import com.saniee.goldlanddcplugin.GoldlandDCPlugin;
import me.leoko.advancedban.manager.PunishmentManager;
import me.leoko.advancedban.manager.UUIDManager;
import me.leoko.advancedban.utils.Punishment;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Locale;

public class info extends ListenerAdapter {
    private final GoldlandDCPlugin plugin;
    JDA Bot;

    public info(GoldlandDCPlugin plugin, JDA Bot) {
        this.Bot = Bot;
        this.plugin = plugin;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User author = event.getAuthor();
        Message message = event.getMessage();
        String[] args = event.getMessage().getContentRaw().split(" ");

        if (!message.getAuthor().isBot() && message.getContentRaw().toLowerCase(Locale.ROOT).startsWith(".info")) {
            if (args.length >= 2) {
                Player player = plugin.getServer().getPlayer(args[1]);
                if (player == null) {
                    event.getChannel().sendMessage("Player offline. Please use command .lookup {name}.").queue();
                    return;
                }

                BigDecimal money = null;
                Object kills = null;
                Object deaths = null;
                Object quit = null;
                Boolean isMuted = false;
                Punishment mute = null;
                String UUID = UUIDManager.get().getUUID(player.getName());

                try {
                    money = Economy.getMoneyExact(player.getUniqueId());
                    kills = player.getStatistic(Statistic.PLAYER_KILLS);
                    deaths = player.getStatistic(Statistic.DEATHS);
                    quit = player.getStatistic(Statistic.LEAVE_GAME);

                    isMuted = PunishmentManager.get().isMuted(String.valueOf(UUID));
                    if (isMuted) {
                        mute = PunishmentManager.get().getMute(UUID);
                    }
                } catch (UserDoesNotExistException e) {
                    e.printStackTrace();
                }

                EmbedBuilder ed = new EmbedBuilder();
                    ed.setTitle("Info About: " + player.getName());
                    ed.setColor(Color.YELLOW);
                assert money != null;
                ed.addField("Money", String.valueOf(money.setScale(1, RoundingMode.HALF_UP)), false);
                    ed.addField("Kills", String.valueOf(kills), false);
                    ed.addField("Deaths", String.valueOf(deaths), false);
                    ed.addField("Quit", quit + " times.", false);
                    if (isMuted) {
                        ed.addField("Punishments", "All recent punishments go under here.", false);
                    }
                    if (isMuted) {
                        ed.addField("Mute", String.format("Reason: %s | Issued by: %s | Duration: %s", mute.getReason(), mute.getOperator(), mute.getDuration(true)), false);
                    }
                    ed.setFooter("Requested By: " + author.getAsTag());
                    ed.setTimestamp(Instant.now());
                    event.getChannel().sendMessage(ed.build()).queue();
            } else {
                event.getChannel().sendMessage("No player name Entered!").queue();
            }
        }
    }
}
