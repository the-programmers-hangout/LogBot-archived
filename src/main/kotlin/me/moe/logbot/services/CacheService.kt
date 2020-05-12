package me.moe.logbot.services

import me.aberrantfox.kjdautils.api.annotation.Service
import me.moe.logbot.data.Configuration
import me.moe.logbot.util.types.LimitedList
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Message

@Service
class CacheService(private val configuration: Configuration) {
    private val messageCaches: Map<String, LimitedList<Message>> = configuration.guildConfigurations
            .associateBy({ it.guildId }, { LimitedList<Message>(it.messageCacheAmount) })

    fun setCacheLimit(guild: Guild, newLimit: Int) {
        messageCaches[guild.id]?.changeLimit(newLimit)
    }

    fun getMessageCache(guild: Guild): LimitedList<Message>? {
        return messageCaches[guild.id]
    }

    fun getMessageFromCache(guild: Guild, messageID: String): Message? {
        return messageCaches[guild.id]?.find { it.id == messageID }
    }

    fun addMessageToCache(guild: Guild, message: Message) {
        messageCaches[guild.id]?.add(message)
    }

    fun removeMessageFromCache(guild: Guild, message: Message) {
        messageCaches[guild.id]?.remove(message)
    }
}