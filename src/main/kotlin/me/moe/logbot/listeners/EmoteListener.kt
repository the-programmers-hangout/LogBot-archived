package me.moe.logbot.listeners

import com.google.common.eventbus.Subscribe
import me.moe.logbot.data.Configuration
import me.moe.logbot.services.LoggingService
import net.dv8tion.jda.api.events.emote.EmoteAddedEvent
import net.dv8tion.jda.api.events.emote.EmoteRemovedEvent
import net.dv8tion.jda.api.events.emote.update.EmoteUpdateNameEvent

class EmoteListener(private val configuration: Configuration, private val logger: LoggingService) {

    @Subscribe
    fun onEmoteAdded(event: EmoteAddedEvent) {
        val config = configuration.getGuildConfig(event.guild.id)
                ?: return

        if (config.trackEmotes)
            logger.buildEmoteLoggerEmbed(event.guild, event.emote, true)
    }

    @Subscribe
    fun onEmoteRemoved(event: EmoteRemovedEvent) {
        val config = configuration.getGuildConfig(event.guild.id)
                ?: return

        if (config.trackEmotes)
            logger.buildEmoteLoggerEmbed(event.guild, event.emote, false)
    }

    @Subscribe
    fun onEmoteChanged(event: EmoteUpdateNameEvent) {
        val config = configuration.getGuildConfig(event.guild.id)
                ?: return

        if (config.trackEmotes)
            logger.buildEmoteRenameEmbed(event.guild, event)
    }
}