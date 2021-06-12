package com.saniee.goldlanddcplugin;

import me.badbones69.crazyauctions.api.events.AuctionBuyEvent;
import me.badbones69.crazyauctions.api.events.AuctionCancelledEvent;
import me.badbones69.crazyauctions.api.events.AuctionExpireEvent;
import me.badbones69.crazyauctions.api.events.AuctionListEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.awt.*;
import java.time.Instant;
import java.util.List;

public class AhToDC implements Listener {
    private final GoldlandDCPlugin plugin;

    JDA Bot;
    TextChannel channel;

    public AhToDC(GoldlandDCPlugin plugin, JDA Bot) {
        this.plugin = plugin;
        this.Bot = Bot;

        String id = plugin.getConfig().getString("AhChatId");
        channel = Bot.getTextChannelById(id);
    }

    @EventHandler
    public void onBuy(AuctionBuyEvent buyEvent) {
        Player player = buyEvent.getPlayer();

        EmbedBuilder buyEmbed = new EmbedBuilder();
            buyEmbed.setColor(Color.YELLOW);
            buyEmbed.setTitle(String.format("User %s bought %s [%s]", player.getName(), buyEvent.getItem().getType(), buyEvent.getItem().getAmount()));
            if (buyEvent.getItem().getItemMeta().hasDisplayName()) {
                String displayName = buyEvent.getItem().getItemMeta().getDisplayName();
                displayName = ChatColor.stripColor(displayName);
                buyEmbed.setTitle(String.format("User %s bought %s [%s]", displayName, buyEvent.getItem().getAmount()));
                buyEmbed.addField("Item Name", String.format("%s", displayName), false);
            } else {
                buyEmbed.setTitle(String.format("User %s bought %s [%s]", player.getName(), buyEvent.getItem().getType().toString().toLowerCase(), buyEvent.getItem().getAmount()));
            }
            buyEmbed.addField("Buy price", String.format("%s $", buyEvent.getPrice()), false);
            buyEmbed.setTimestamp(Instant.now());

        channel.sendMessage(buyEmbed.build()).queue();
    }

    @EventHandler
    public void onSell(AuctionListEvent sellEvent) {
        Player player = sellEvent.getPlayer();

        EmbedBuilder sellEmbed = new EmbedBuilder();
            sellEmbed.setColor(Color.YELLOW);
            if (sellEvent.getItem().getItemMeta().hasDisplayName()) {
                String displayName = sellEvent.getItem().getItemMeta().getDisplayName();
                displayName = ChatColor.stripColor(displayName);
                sellEmbed.setTitle(String.format("User %s listed %s [%s]", player.getName(), displayName, sellEvent.getItem().getAmount()));
                sellEmbed.addField("Item Name", String.format("%s", displayName), false);
            } else {
                sellEmbed.setTitle(String.format("User %s listed %s [%s]", player.getName(), sellEvent.getItem().getType().toString().toLowerCase(), sellEvent.getItem().getAmount()));
            }
            if (sellEvent.getItem().getItemMeta().hasLore()) {
                List<String> lore = sellEvent.getItem().getItemMeta().getLore();
                for (int i = 0; i < lore.toArray().length; i++) {
                    String[] loreArray = lore.toArray(new String[i]);
                    loreArray[i] = ChatColor.stripColor(loreArray[i]);
                    if (!loreArray[i].isEmpty()) {
                        sellEmbed.addField(String.format("Lore Line %d", i), loreArray[i], false);
                    }
                }
            }
            sellEmbed.addField("Listing price", String.format("%s $", sellEvent.getPrice()), false);
            sellEmbed.setTimestamp(Instant.now());

        channel.sendMessage(sellEmbed.build()).queue();
    }

    @EventHandler
    public void onExpire(AuctionExpireEvent expireEvent) {
        Player player = expireEvent.getOnlinePlayer();
        if (player == null) {
            OfflinePlayer offlinePlayer = expireEvent.getOfflinePlayer();

            EmbedBuilder expireEmbed = new EmbedBuilder();
                expireEmbed.setColor(Color.YELLOW);
                if (expireEvent.getItem().getItemMeta().hasDisplayName()) {
                    String displayName = expireEvent.getItem().getItemMeta().getDisplayName();
                    displayName = ChatColor.stripColor(displayName);
                    expireEmbed.setTitle(String.format("User's [%s] listing of %s [%s] expired", offlinePlayer.getName(), displayName, expireEvent.getItem().getAmount()));
                } else {
                    expireEmbed.setTitle(String.format("User's [%s] listing of %s [%s] expired", offlinePlayer.getName(), expireEvent.getItem().getType().toString().toLowerCase(), expireEvent.getItem().getAmount()));
                }
                expireEmbed.setTimestamp(Instant.now());

            channel.sendMessage(expireEmbed.build()).queue();
        } else {
            EmbedBuilder expireEmbed = new EmbedBuilder();
                expireEmbed.setColor(Color.YELLOW);
                if (expireEvent.getItem().getItemMeta().hasDisplayName()) {
                    String displayName = expireEvent.getItem().getItemMeta().getDisplayName();
                    displayName = ChatColor.stripColor(displayName);
                    expireEmbed.setTitle(String.format("User's [%s] listing of %s [%s] expired", player.getName(), displayName, expireEvent.getItem().getAmount()));
                } else {
                    expireEmbed.setTitle(String.format("User's [%s] listing of %s [%s] expired", player.getName(), expireEvent.getItem().getType().toString().toLowerCase(), expireEvent.getItem().getAmount()));
                }
                expireEmbed.setTimestamp(Instant.now());

            channel.sendMessage(expireEmbed.build()).queue();
        }
    }

    @EventHandler
    public void onCancelled(AuctionCancelledEvent cancelledEvent) {
        Player player = cancelledEvent.getOnlinePlayer();
        if (player == null) {
            OfflinePlayer offlinePlayer = cancelledEvent.getOfflinePlayer();

            EmbedBuilder cancelledEmbed = new EmbedBuilder();
                cancelledEmbed.setColor(Color.YELLOW);
                if (cancelledEvent.getItem().getItemMeta().hasDisplayName()) {
                    String displayName = cancelledEvent.getItem().getItemMeta().getDisplayName();
                    displayName = ChatColor.stripColor(displayName);
                    cancelledEmbed.setTitle(String.format("User %s stopped listing of %s [%s]", offlinePlayer.getName(), displayName, cancelledEvent.getItem().getAmount()));
                } else {
                    cancelledEmbed.setTitle(String.format("User %s stopped listing of %s [%s]", offlinePlayer.getName(), cancelledEvent.getItem().getType().toString().toLowerCase(), cancelledEvent.getItem().getAmount()));
                }
                cancelledEmbed.setTimestamp(Instant.now());

            channel.sendMessage(cancelledEmbed.build()).queue();
        } else {
            EmbedBuilder cancelledEmbed = new EmbedBuilder();
                cancelledEmbed.setColor(Color.YELLOW);
                if (cancelledEvent.getItem().getItemMeta().hasDisplayName()) {
                    String displayName = cancelledEvent.getItem().getItemMeta().getDisplayName();
                    displayName = ChatColor.stripColor(displayName);
                    cancelledEmbed.setTitle(String.format("User %s stopped listing of %s [%s]", player.getName(), displayName, cancelledEvent.getItem().getAmount()));
                } else {
                    cancelledEmbed.setTitle(String.format("User %s stopped listing of %s [%s]", player.getName(), cancelledEvent.getItem().getType().toString().toLowerCase(), cancelledEvent.getItem().getAmount()));
                }
                cancelledEmbed.setTimestamp(Instant.now());

            channel.sendMessage(cancelledEmbed.build()).queue();
        }
    }
}
