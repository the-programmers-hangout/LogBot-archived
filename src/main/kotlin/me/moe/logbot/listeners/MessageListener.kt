package me.moe.logbot.listeners

import com.google.common.eventbus.Subscribe
import me.aberrantfox.kjdautils.extensions.jda.toMember
import me.moe.logbot.data.Configuration
import me.moe.logbot.data.GuildConfiguration
import me.moe.logbot.services.CacheService
import me.moe.logbot.services.LoggingService
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent

class MessageListener(val configuration: Configuration, private val logger: LoggingService,
                      val cacheService: CacheService) {

    @Subscribe
    fun onGuildMessageRecieved(event: GuildMessageReceivedEvent) {
        if (event.author.isBot) return
        if (event.author.toMember(event.guild) == null) return
        if (event.message.contentRaw.startsWith(configuration.prefix)) return

        val config = configuration.getGuildConfig(event.guild.id)
                ?: return

        if (shouldBeLogged(event.author.toMember(event.guild)!!, config)) return

        if (configuration.isTrackingMessages(event.guild.id)) {
            cacheService.addMessageToCache(event.guild, event.message)
        }
    }

    @Subscribe
    fun onGuildMessageEdited(event: GuildMessageUpdateEvent) {
        if (event.author.isBot) return
        if (event.author.toMember(event.guild) == null) return

        val config = configuration.getGuildConfig(event.guild.id)
                ?: return

        if (shouldBeLogged(event.author.toMember(event.guild)!!, config)) return

        val cachedMessage = cacheService.getMessageFromCache(event.guild, event.messageIdLong) ?: return

        if (cachedMessage.contentRaw == event.message.contentRaw) return

        if (configuration.isTrackingMessages(event.guild.id)) {
            logger.buildMessageEditedEmbed(event, cachedMessage)
        }

        cacheService.removeMessageFromCache(event.guild, cachedMessage)
        cacheService.addMessageToCache(event.guild, event.message)
    }

    @Subscribe
    fun onGuildMessageDelete(event: GuildMessageDeleteEvent) {
        val cachedMessage = cacheService.getMessageFromCache(event.guild, event.messageIdLong) ?: return

        val author = cachedMessage.author
        if (author.isBot) return
        if (author.toMember(event.guild) == null) return

        val config = configuration.getGuildConfig(event.guild.id)
                ?: return

        if (shouldBeLogged(author.toMember(event.guild)!!, config)) return

        if (configuration.isTrackingMessages(event.guild.id)) {
            logger.buildMessageDeletedEmbed(event, cachedMessage)
        }
    }


    private fun shouldBeLogged(member: Member, guildConfig: GuildConfiguration): Boolean {
        return guildConfig.ignoreRoleNames.intersect(member.roles.map { it.name }).isNotEmpty()
    }
}