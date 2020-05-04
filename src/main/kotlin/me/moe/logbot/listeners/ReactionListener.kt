package me.moe.logbot.listeners

import com.google.common.eventbus.Subscribe
import me.moe.logbot.data.Configuration
import me.moe.logbot.services.LoggingService
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent

class ReactionListener(private val configuration: Configuration, private val logger: LoggingService) {

    @Subscribe
    fun onReactionAdd(event: GuildMessageReactionAddEvent) {
        if (event.member.user.isBot) return
        val config = configuration.getGuildConfig(event.guild.id)
                ?: return

        if (config.trackReactions)
            logger.buildReactionAddedEvent(event)
    }

    @Subscribe
    fun onReactionRemove(event: GuildMessageReactionRemoveEvent) {
        val config = configuration.getGuildConfig(event.guild.id)
                ?: return

        if (config.trackReactions)
            logger.buildReactionRemovedEvent(event)
    }
}