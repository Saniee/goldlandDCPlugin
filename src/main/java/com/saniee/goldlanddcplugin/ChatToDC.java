package com.saniee.goldlanddcplugin;

import me.leoko.advancedban.manager.PunishmentManager;
import me.leoko.advancedban.manager.UUIDManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.awt.*;
import java.time.Instant;

/* TODO
* Fix Sending Embeds when the user is muted.
* Make the logic for deciding if the player is muted better and efficient
*/

public class ChatToDC implements Listener {
    private final GoldlandDCPlugin plugin;

    JDA Bot;
    TextChannel channel;

    public ChatToDC (GoldlandDCPlugin plugin, JDA Bot) {
        this.Bot = Bot;
        this.plugin = plugin;

        String id = plugin.getConfig().getString("McChatId");
        channel = Bot.getTextChannelById(id);
    }

    @EventHandler
    public void DConChat (AsyncPlayerChatEvent em) {
        Player player = em.getPlayer();
        String UUID = UUIDManager.get().getUUID(String.valueOf(em.getPlayer()));

        EmbedBuilder chmb = new EmbedBuilder();
            chmb.setColor(Color.YELLOW);
            chmb.setDescription(player.getName() + " » " + em.getMessage());
            chmb.setTimestamp(Instant.now());
        boolean IsMuted = PunishmentManager.get().isMuted(UUID);
        try {
            if (IsMuted || em.isCancelled()) {
                plugin.getLogger().info("Player Muted. Not Sending Embed!");
            } else {
                channel.sendMessage(chmb.build()).queue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void DConPlayerDeath(PlayerDeathEvent ed) {
        EmbedBuilder chdb = new EmbedBuilder();
            chdb.setDescription(ed.getEntity().getName() + " Died.");
            chdb.setTimestamp(Instant.now());
        try {
            channel.sendMessage(chdb.build()).queue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
