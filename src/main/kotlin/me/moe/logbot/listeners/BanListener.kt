package me.moe.logbot.listeners

import com.google.common.eventbus.Subscribe
import me.moe.logbot.data.Configuration
import me.moe.logbot.services.LoggingService
import net.dv8tion.jda.api.events.guild.GuildBanEvent
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent

class BanListener(private val configuration: Configuration, private val logger: LoggingService) {

    @Subscribe
    fun onMemberUnbanned(event: GuildUnbanEvent) {
        val config = configuration.getGuildConfig(event.guild.id)
                ?: return

        if (config.trackBans)
            logger.buildMemberUnbanEmbed(event)
    }
}