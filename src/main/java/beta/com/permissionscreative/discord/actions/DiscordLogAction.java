package beta.com.permissionscreative.discord.actions;

import beta.com.permissionscreative.configuration.Config;
import beta.com.permissionscreative.discord.DiscordBot;
import beta.com.permissionscreative.languagemanager.LangManager;
import beta.com.permissionscreative.object.EventsType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.awt.*;

/**
 * The DiscordLogAction class is responsible for logging actions to a Discord channel.
 *
 * It has three private members:
 * - `discordBot`: an instance of the DiscordBot class, used to interact with the Discord API.
 * - `config`: an instance of the Config class, used to get configuration details.
 * - `langManager`: an instance of LangManager to handle language-specific messages.
 *
 * The class constructor initializes these members.
 *
 * The `logToChannel` method is a private method that sends a message to a specific Discord channel. The message is an embed with a title, description, and color.
 *
 * The `logAction` method checks if logging is enabled in the configuration. If it is, it gets the channel ID from the configuration and sends a message to that channel using the `logToChannel` method.
 */

public class DiscordLogAction {
    private final DiscordBot discordBot;
    private final Config config;
    private final LangManager langManager;

    public DiscordLogAction(DiscordBot discordBot, Config config, LangManager langManager) {
        this.discordBot = discordBot;
        this.config = config;
        this.langManager = langManager;
    }

    private void logToChannel(TextChannel channel, EventsType eventsType) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle(eventsType.getAction())
                .setDescription(eventsType.getMessage())
                .setColor(Color.RED);

        channel.sendMessageEmbeds(embedBuilder.build()).queue();
    }

    public void logAction(EventsType eventsType) {
        if (!config.getConfig().getBoolean("logging.discordbot.enabled")) {
            return;
        }

        String channelId = config.getConfig().getString("logging.discordbot.channel_id");
        if (channelId == null || channelId.isEmpty()) {
            System.out.println(langManager.getMessage("discord.events.channel_not_found", config.getConfig().getString("lang")));
            return;
        }

        TextChannel channel = discordBot.getJDA().getTextChannelById(channelId);
        if (channel != null) {
            logToChannel(channel, eventsType);
        }
    }
}