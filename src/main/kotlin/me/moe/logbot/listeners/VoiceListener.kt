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
        if (configuration.isTrackingVoice(event.guild.id))
            logger.buildVoiceJoinEmbed(event)
    }

    @Subscribe
    fun onVoiceLeave(event: GuildVoiceLeaveEvent) {
        if (configuration.isTrackingVoice(event.guild.id))
            logger.buildVoiceLeaveEmbed(event)
    }

    @Subscribe
    fun onVoiceMove(event: GuildVoiceMoveEvent) {
        if (configuration.isTrackingVoice(event.guild.id))
            logger.buildVoiceMoveEmbed(event)
    }
}