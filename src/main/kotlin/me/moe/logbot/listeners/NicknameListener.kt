package me.moe.logbot.listeners

import com.google.common.eventbus.Subscribe
import me.moe.logbot.data.Configuration
import me.moe.logbot.services.LoggingService
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent

class NicknameListener(private val configuration: Configuration, private val logger: LoggingService) {

    @Subscribe
    fun onRoleAdd(event: GuildMemberUpdateNicknameEvent) {
        val config = configuration.getGuildConfig(event.guild.id)
                ?: return

        if (config.trackNicknames)
            logger.buildNicknameChangeEmbed(event)
    }
}