package me.moe.logbot.services

import com.google.common.collect.EvictingQueue
import me.aberrantfox.kjdautils.api.annotation.Service
import me.moe.logbot.data.Configuration
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Message

@Service
class CacheService(private val configuration: Configuration) {
    private val messageCaches: MutableMap<Long, EvictingQueue<Pair<Long, Message>>> = mutableMapOf()

    fun addMessageToCache(guild: Guild, message: Message) {
        if (!messageCaches.containsKey(guild.idLong))
            messageCaches[guild.idLong] = EvictingQueue.create(configuration.cacheAmount)

        messageCaches[guild.idLong]!!.add(message.idLong to message)
    }

    fun getMessageFromCache(guild: Guild, messageId: Long): Message? {
        return messageCaches[guild.idLong]?.find { it.first == messageId }?.second
    }

    fun removeMessageFromCache(guild: Guild, message: Message) {
        messageCaches[guild.idLong]?.remove(message.idLong to message)
    }
}