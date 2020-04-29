package me.moe.logbot.listeners

import com.google.common.eventbus.Subscribe
import me.moe.logbot.data.Configuration
import me.moe.logbot.services.LoggingService
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent

class MemberListener(private val configuration: Configuration, private val logger: LoggingService) {

    @Subscribe
    fun onMemberJoin(event: GuildMemberJoinEvent) {
        val config = configuration.getGuildConfig(event.guild.id)
                ?: return

        if (config.trackMembers)
            logger.buildMemberJoinEmbed(event)
    }

    @Subscribe
    fun onGuildMemberJoinEvent(event: GuildMemberJoinEvent) {
        print(event)
        val member = event.member
        val user = event.user
        val guild = event.guild

    }

    @Subscribe
    fun onMemberLeave(event: GuildMemberLeaveEvent) {
        val config = configuration.getGuildConfig(event.guild.id)
                ?: return

        if (config.trackMembers)
            logger.buildMemberLeaveEmbed(event)
    }
}