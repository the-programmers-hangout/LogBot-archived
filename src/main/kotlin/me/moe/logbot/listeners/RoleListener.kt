package me.moe.logbot.listeners

import com.google.common.eventbus.Subscribe
import me.moe.logbot.data.Configuration
import me.moe.logbot.services.LoggingService
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent

class RoleListener(private val configuration: Configuration, private val logger: LoggingService) {

    @Subscribe
    fun onRoleAdd(event: GuildMemberRoleAddEvent) {
        val config = configuration.getGuildConfig(event.guild.id)
                ?: return

        if (config.trackMembers)
            logger.buildRoleAddEmbed(event)
    }

    @Subscribe
    fun onRoleRemove(event: GuildMemberRoleRemoveEvent) {
        val config = configuration.getGuildConfig(event.guild.id)
                ?: return

        if (config.trackMembers)
            logger.buildRoleRemoveEmbed(event)
    }
}