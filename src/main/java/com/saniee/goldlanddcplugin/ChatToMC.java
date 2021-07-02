package com.saniee.goldlanddcplugin;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

/* TODO
* Errors out sometimes out of not proper asynchrounous calls.
*/

public class ChatToMC extends ListenerAdapter {
    private final GoldlandDCPlugin plugin;
    JDA Bot;
    TextChannel channel;

    public ChatToMC(GoldlandDCPlugin plugin, JDA Bot) {
        this.Bot = Bot;
        this.plugin = plugin;

        String id = plugin.getConfig().getString("McChatId");
        channel = Bot.getTextChannelById(id);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent ev) {
        Message message = ev.getMessage();
        User user = ev.getAuthor();

        if (!user.isBot() && message.getChannel() == channel)
            try {
                plugin.getServer().broadcastMessage(String.format("§7[§6Gold§eLand§9DC§7] §7%s §6%s §8» §f%s", Objects.requireNonNull(message.getMember()).getRoles().get(0).getName(), user.getAsTag(), message.getContentRaw()));
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}

