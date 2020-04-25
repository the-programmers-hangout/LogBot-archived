package me.moe.logbot.listeners

import com.google.common.eventbus.Subscribe
import me.moe.logbot.data.Configuration
import me.moe.logbot.services.LoggingService
import net.dv8tion.jda.api.events.emote.EmoteAddedEvent
import net.dv8tion.jda.api.events.emote.EmoteRemovedEvent
import net.dv8tion.jda.api.events.emote.update.EmoteUpdateNameEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent

class MemberListener(private val configuration: Configuration, private val logger: LoggingService) {

    @Subscribe
    fun onMemberJoin(event: GuildMemberJoinEvent) {
        val config = configuration.getGuildConfig(event.guild.id)
                ?: return

        if (config.trackMembers)
            logger.buildMemberLoggerEmbed(event.guild, event.user, true)
    }

    @Subscribe
    fun onMemberLeave(event: GuildMemberLeaveEvent) {
        val config = configuration.getGuildConfig(event.guild.id)
                ?: return

        if (config.trackMembers)
            logger.buildMemberLoggerEmbed(event.guild, event.user, false)
    }
}