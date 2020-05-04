package me.moe.logbot.listeners

import com.google.common.eventbus.Subscribe
import me.moe.logbot.data.Configuration
import me.moe.logbot.services.LoggingService
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent

class VoiceListener(private val configuration: Configuration, private val logger: LoggingService) {

    @Subscribe
    fun onVoiceJoin(event: GuildVoiceJoinEvent) {
        val config = configuration.getGuildConfig(event.guild.id)
                ?: return

        if (config.trackVoice)
            logger.buildVoiceJoinEmbed(event)
    }

    @Subscribe
    fun onVoiceLeave(event: GuildVoiceLeaveEvent) {
        val config = configuration.getGuildConfig(event.guild.id)
                ?: return

        if (config.trackVoice)
            logger.buildVoiceLeaveEmbed(event)
    }

    @Subscribe
    fun onVoiceMove(event: GuildVoiceMoveEvent) {
        val config = configuration.getGuildConfig(event.guild.id)
                ?: return

        if (config.trackVoice)
            logger.buildVoiceMoveEmbed(event)
    }
}