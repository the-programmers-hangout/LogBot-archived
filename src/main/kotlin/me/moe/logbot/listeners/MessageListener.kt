package me.moe.logbot.listeners

import com.google.common.eventbus.Subscribe
import me.aberrantfox.kjdautils.extensions.jda.toMember
import me.moe.logbot.data.Configuration
import me.moe.logbot.data.GuildConfiguration
import me.moe.logbot.services.LoggingService
import me.moe.logbot.util.types.LimitedList
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent

class MessageListener(val configuration: Configuration, private val logger: LoggingService) {

    private val messageCaches: Map<String, LimitedList<Message>> = configuration.guildConfigurations
            .associateBy({ it.guildId }, { LimitedList<Message>(it.messageCacheAmount) })

    @Subscribe
    fun onGuildMessageRecieved(event: GuildMessageReceivedEvent) {
        if (event.author.isBot) return
        if (event.author.toMember(event.guild) == null) return
        if (event.message.contentRaw.startsWith(configuration.prefix)) return

        val config = configuration.getGuildConfig(event.guild.id)
                ?: return

        if (shouldBeLogged(event.author.toMember(event.guild)!!, config)) return

        if (config.trackMessages) {
            messageCaches[event.guild.id]?.add(event.message)
        }
    }

    @Subscribe
    fun onGuildMessageEdited(event: GuildMessageUpdateEvent) {
        if (event.author.isBot) return
        if (event.author.toMember(event.guild) == null) return

        val config = configuration.getGuildConfig(event.guild.id)
                ?: return

        if (shouldBeLogged(event.author.toMember(event.guild)!!, config)) return

        val messageCache: LimitedList<Message> = messageCaches[event.guild.id] ?: return
        val cachedMessage = messageCaches[event.guild.id]?.find { it.id == event.messageId } ?: return

        logger.buildMessageEditedEmbed(event, cachedMessage)

        messageCache.remove(cachedMessage)
        messageCache.add(event.message)
    }

    @Subscribe
    fun onGuildMessageDelete(event: GuildMessageDeleteEvent) {
        val messageCache: LimitedList<Message> = messageCaches[event.guild.id] ?: return
        val cachedMessage = messageCaches[event.guild.id]?.find { it.id == event.messageId } ?: return

        logger.buildMessageDeletedEmbed(event, cachedMessage)
    }


    private fun shouldBeLogged(member: Member, guildConfig: GuildConfiguration): Boolean {
        return guildConfig.ignoreRoleNames.intersect(member.roles.map { it.name }).isNotEmpty()
    }
}