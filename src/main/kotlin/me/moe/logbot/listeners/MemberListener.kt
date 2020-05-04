package me.moe.logbot.listeners

import com.google.common.eventbus.Subscribe
import me.moe.logbot.data.Configuration
import me.moe.logbot.services.LoggingService
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent

class MemberListener(private val configuration: Configuration, private val logger: LoggingService) {

    @Subscribe
    fun onMemberJoin(event: GuildMemberJoinEvent) {
        val config = configuration.getGuildConfig(event.guild.id)
                ?: return

        if (config.trackMembers)
            logger.buildMemberJoinEmbed(event)
    }

    @Subscribe
    fun onMemberLeave(event: GuildMemberRemoveEvent) {
        val config = configuration.getGuildConfig(event.guild.id)
                ?: return

        if (event.guild.retrieveBanList().complete().any { it.user.id == event.user.id }) {
            if (config.trackBans)
                logger.buildMemberBanEmbed(event)
        } else {
            if (config.trackMembers)
                logger.buildMemberLeaveEmbed(event)
        }

    }
}