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
        if (configuration.isTrackingEmotes(event.guild.id))
            logger.buildEmoteAddedEmbed(event)
    }

    @Subscribe
    fun onEmoteRemoved(event: EmoteRemovedEvent) {
        if (configuration.isTrackingEmotes(event.guild.id))
            logger.buildEmoteDeletedEmbed(event)
    }

    @Subscribe
    fun onEmoteChanged(event: EmoteUpdateNameEvent) {
        if (configuration.isTrackingEmotes(event.guild.id))
            logger.buildEmoteRenameEmbed(event)
    }
}