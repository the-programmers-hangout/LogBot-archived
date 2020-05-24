package me.moe.logbot.listeners

import com.google.common.eventbus.Subscribe
import me.moe.logbot.data.Configuration
import me.moe.logbot.services.LoggingService
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateNameEvent
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateSlowmodeEvent
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateTopicEvent
import net.dv8tion.jda.api.events.guild.GuildBanEvent
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent

class ChannelListener(private val configuration: Configuration, private val logger: LoggingService) {

    @Subscribe
    fun onSlowmodeUpdate(event: TextChannelUpdateSlowmodeEvent) {
        if (configuration.isTrackingChannels(event.guild.id))
            logger.buildSlowmodeUpdateEmbed(event)
    }

    @Subscribe
    fun onTextChannelCreate(event: TextChannelCreateEvent) {
        if (configuration.isTrackingChannels(event.guild.id))
            logger.buildChannelCreateEmbed(event)
    }

    @Subscribe
    fun onTextChannelDelete(event: TextChannelDeleteEvent) {
        if (configuration.isTrackingChannels(event.guild.id))
            logger.buildChannelDeleteEmbed(event)
    }

    @Subscribe
    fun onTextChannelUpdateName(event: TextChannelUpdateNameEvent) {
        if (configuration.isTrackingChannels(event.guild.id))
            logger.buildChannelUpdateNameEmbed(event)
    }

    @Subscribe
    fun onTextChannelUpdateTopicEvent(event: TextChannelUpdateTopicEvent) {
        // only ever null if name is changed on a new channel when no topic is set
        if (event.newTopic!!.isBlank() && event.oldTopic.isNullOrBlank()) {
            return
        }

        if (configuration.isTrackingChannels(event.guild.id))
            logger.buildChannelUpdateTopicEmbed(event)
    }
}