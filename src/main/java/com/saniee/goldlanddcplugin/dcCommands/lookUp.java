package com.saniee.goldlanddcplugin.dcCommands;

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
import net.ess3.api.Economy;
import org.bukkit.OfflinePlayer;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;

/* TODO
* Make the playtime work somehow.
*/

public class lookUp extends ListenerAdapter {
    private final GoldlandDCPlugin plugin;
    JDA Bot;

    public lookUp(GoldlandDCPlugin plugin, JDA Bot) {
        this.Bot = Bot;
        this.plugin = plugin;
    }

   @Override
   public void onMessageReceived(MessageReceivedEvent ev) {
        User user = ev.getAuthor();
        Message message = ev.getMessage();
        String[] args = ev.getMessage().getContentRaw().split(" ");

       if (!user.isBot() && message.getContentRaw().toLowerCase(Locale.ROOT).startsWith(".lookup")) {
            if (args.length >= 2) {
                OfflinePlayer[] offlinePlayer = plugin.getServer().getOfflinePlayers();
                for (OfflinePlayer value : offlinePlayer) {
                    OfflinePlayer player = plugin.getServer().getOfflinePlayer(value.getUniqueId());
                    if (player.getName().equals(args[1])) {
                        String UUID = UUIDManager.get().getUUID(player.getName());
                        boolean isMuted = false;
                        boolean isBanned = false;
                        BigDecimal money = null;
                        Punishment mute = null;
                        Punishment ban = null;
                        Date lastSeen = null;
                        //String Playtime = null

                        try {
                            isMuted = PunishmentManager.get().isMuted(UUID);
                            isBanned = PunishmentManager.get().isBanned(UUID);
                            money = Economy.getMoneyExact(player.getUniqueId());

                            if (isMuted) {
                                mute = PunishmentManager.get().getMute(UUID);
                            }
                            if (isBanned) {
                                ban = PunishmentManager.get().getBan(UUID);
                            }
                            long mil = player.getLastPlayed();
                            lastSeen = new Date(mil);
                            long lastPlayed = player.getFirstPlayed();
                            //long date = System.currentTimeMillis() - lastPlayed;
                            //Playtime = String.format("Hours: %d | Minutes: %d | Seconds: %d", date / (60 * 60 * 1000) % 24, date / (60 * 1000) % 60, date / 1000 % 60);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }



                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setTitle("Info about " + player.getName());
                        eb.setColor(Color.YELLOW);
                        eb.addField("Last Seen", String.valueOf(lastSeen), false);
                        assert money != null;
                        eb.addField("Money", String.valueOf(money.setScale(1, RoundingMode.HALF_UP)), false);
                        if (isBanned || isMuted) {
                            eb.addField("Punishments", "All recent punishments go under here.", false);
                        }
                        if (isMuted) {
                            assert mute != null;
                            eb.addField("Mute", String.format("Reason: %s | Muted by: %s | Duration: %s", mute.getReason(), mute.getOperator(), mute.getDuration(true)), false);
                        }
                        if (isBanned) {
                            assert ban != null;
                            eb.addField("Ban", String.format("Reason: %s | Banned by: %s | Duration: %s", ban.getReason(), ban.getOperator(), ban.getDuration(true)), false);
                        }
                        eb.setFooter("Requested by: " + ev.getAuthor().getAsTag());
                        eb.setTimestamp(Instant.now());
                        //plugin.getLogger().info(Playtime);
                        message.getChannel().sendMessage(eb.build()).queue();
                    }
                }
            } else {
                ev.getChannel().sendMessage("No player name Entered!").queue();
            }
        }
   }
}