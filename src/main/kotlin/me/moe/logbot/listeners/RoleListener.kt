package me.moe.logbot.listeners

import com.google.common.eventbus.Subscribe
import me.moe.logbot.data.Configuration
import me.moe.logbot.services.LoggingService
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent
import net.dv8tion.jda.api.events.role.RoleCreateEvent
import net.dv8tion.jda.api.events.role.RoleDeleteEvent
import net.dv8tion.jda.api.events.role.update.RoleUpdateColorEvent
import net.dv8tion.jda.api.events.role.update.RoleUpdateNameEvent

class RoleListener(private val configuration: Configuration, private val logger: LoggingService) {

    @Subscribe
    fun onRoleAdd(event: GuildMemberRoleAddEvent) {
        if (configuration.isTrackingRoles(event.guild.id))
            logger.buildRoleAddEmbed(event)
    }

    @Subscribe
    fun onRoleRemove(event: GuildMemberRoleRemoveEvent) {
        if (configuration.isTrackingRoles(event.guild.id))
            logger.buildRoleRemoveEmbed(event)
    }

    @Subscribe
    fun onRoleCreate(event: RoleCreateEvent) {
        if (configuration.isTrackingRoles(event.guild.id))
            logger.buildRoleCreateEmbed(event)
    }

    @Subscribe
    fun onRoleDelete(event: RoleDeleteEvent) {
        if (configuration.isTrackingRoles(event.guild.id))
            logger.buildRoleDeleteEmbed(event)
    }

    @Subscribe
    fun onRoleNameUpdated(event: RoleUpdateNameEvent) {
        if (configuration.isTrackingRoles(event.guild.id))
            logger.buildRoleUpdateNameEmbed(event)
    }

    @Subscribe
    fun onRoleColourUpdated(event: RoleUpdateColorEvent) {
        if (configuration.isTrackingRoles(event.guild.id))
            logger.buildRoleUpdateColourEmbed(event)
    }
}