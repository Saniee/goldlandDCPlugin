package com.saniee.goldlanddcplugin;

import me.leoko.advancedban.bukkit.event.PunishmentEvent;
import me.leoko.advancedban.bukkit.event.RevokePunishmentEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.awt.*;
import java.time.Instant;
import java.util.Locale;

public class BansToDC implements Listener {
    private final GoldlandDCPlugin plugin;

    JDA Bot;
    TextChannel channel;

    public BansToDC(GoldlandDCPlugin plugin, JDA Bot) {
        this.plugin = plugin;
        this.Bot = Bot;

        String id = plugin.getConfig().getString("WarnChatId");
        channel = Bot.getTextChannelById(id);
    }

    @EventHandler
    public void BansToDC(PunishmentEvent event) {
        EmbedBuilder Punembed = new EmbedBuilder();
            Punembed.setColor(Color.RED);
            Punembed.setAuthor("Punishment issued By: " + event.getPunishment().getOperator());
            Punembed.addField("Punishment type:", String.valueOf(event.getPunishment().getType()).toLowerCase(Locale.ROOT), false);
            Punembed.addField("Name of Player Punished:", String.valueOf(event.getPunishment().getName()), false);
            Punembed.addField("Reason:", String.valueOf(event.getPunishment().getReason()), false);
            Punembed.addField("Duration:", String.valueOf(event.getPunishment().getDuration(true)), false);
            Punembed.setFooter("Punishment Issued");
            Punembed.setTimestamp(Instant.now());
        try {
            channel.sendMessage(Punembed.build()).queue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void RevokesToDC(RevokePunishmentEvent event) {
        EmbedBuilder Revembed = new EmbedBuilder();
            Revembed.setColor(Color.GREEN);
            Revembed.setAuthor("Punishment Revoked By: " + event.getPunishment().getOperator());
            Revembed.addField("Revoked Punishment of type: ", String.valueOf(event.getPunishment().getType()).toLowerCase(Locale.ROOT), false);
            Revembed.setFooter("Punishment Revoked");
            Revembed.setTimestamp(Instant.now());
        try {
            channel.sendMessage(Revembed.build()).queue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
