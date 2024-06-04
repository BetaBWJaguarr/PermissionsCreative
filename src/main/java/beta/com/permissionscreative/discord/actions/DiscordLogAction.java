package beta.com.permissionscreative.discord.actions;

import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.discord.DiscordBot;
import beta.com.permissionscreative.languagemanager.LangManager;
import beta.com.permissionscreative.object.EventsType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.awt.*;

public class DiscordLogAction {
    private final DiscordBot discordBot;
    private final Config config;
    private final LangManager langManager;

    public DiscordLogAction(DiscordBot discordBot, Config config, LangManager langManager) {
        this.discordBot = discordBot;
        this.config = config;
        this.langManager = langManager;
    }

    public void logAction(EventsType eventsType) {
        if (config.getConfig().getBoolean("logging.discordbot.enabled")) {

            String channelId = config.getConfig().getString("logging.discordbot.channel_id");
            if (channelId == null || channelId.isEmpty()) {
                System.out.println(langManager.getMessage("discord.events.channel_not_found", config.getConfig().getString("lang")));
                return;
            }

            TextChannel channel = discordBot.getJDA().getTextChannelById(channelId);

            if (channel != null) {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle(eventsType.getAction());
                embedBuilder.setDescription(eventsType.getMessage());
                embedBuilder.setColor(Color.RED);

                channel.sendMessageEmbeds(embedBuilder.build()).queue();
            }
        }
    }
}