package me.moe.logbot.listeners

import com.google.common.eventbus.Subscribe
import me.aberrantfox.kjdautils.extensions.jda.toMember
import me.moe.logbot.data.Configuration
import me.moe.logbot.data.GuildConfiguration
import me.moe.logbot.services.CacheService
import me.moe.logbot.services.LoggingService
import me.moe.logbot.util.types.LimitedList
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent

class MessageListener(val configuration: Configuration, private val logger: LoggingService,
                      val cacheService: CacheService) {

//    private val messageCaches: Map<String, LimitedList<Message>> = configuration.guildConfigurations
//            .associateBy({ it.guildId }, { LimitedList<Message>(it.messageCacheAmount) })

    @Subscribe
    fun onGuildMessageRecieved(event: GuildMessageReceivedEvent) {
        if (event.author.isBot) return
        if (event.author.toMember(event.guild) == null) return
        if (event.message.contentRaw.startsWith(configuration.prefix)) return

        val config = configuration.getGuildConfig(event.guild.id)
                ?: return

        if (shouldBeLogged(event.author.toMember(event.guild)!!, config)) return

        if (config.trackMessages) {
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

        val cachedMessage = cacheService.getMessageFromCache(event.guild, event.messageId) ?: return

        logger.buildMessageEditedEmbed(event, cachedMessage)

        cacheService.removeMessageFromCache(event.guild, cachedMessage)
        cacheService.addMessageToCache(event.guild, event.message)
    }

    @Subscribe
    fun onGuildMessageDelete(event: GuildMessageDeleteEvent) {
        val cachedMessage = cacheService.getMessageFromCache(event.guild, event.messageId) ?: return

        val author = cachedMessage.author
        if (author.isBot) return
        if (author.toMember(event.guild) == null) return

        val config = configuration.getGuildConfig(event.guild.id)
                ?: return

        if (shouldBeLogged(author.toMember(event.guild)!!, config)) return

        logger.buildMessageDeletedEmbed(event, cachedMessage)
    }


    private fun shouldBeLogged(member: Member, guildConfig: GuildConfiguration): Boolean {
        return guildConfig.ignoreRoleNames.intersect(member.roles.map { it.name }).isNotEmpty()
    }
}