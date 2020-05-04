package me.moe.logbot.conversations

import me.aberrantfox.kjdautils.api.dsl.Conversation
import me.aberrantfox.kjdautils.api.dsl.conversation
import me.aberrantfox.kjdautils.api.getInjectionObject
import me.aberrantfox.kjdautils.extensions.jda.getRoleByIdOrName
import me.aberrantfox.kjdautils.extensions.jda.getRoleByName
import me.aberrantfox.kjdautils.internal.arguments.IntegerArg
import me.aberrantfox.kjdautils.internal.arguments.SentenceArg
import me.aberrantfox.kjdautils.internal.arguments.TextChannelArg
import me.aberrantfox.kjdautils.internal.services.PersistenceService
import me.moe.logbot.data.Configuration
import me.moe.logbot.data.GuildConfiguration
import net.dv8tion.jda.api.entities.Guild

class GuildSetupConversation(): Conversation() {
    @Start
    fun createGuildSetupConversation(guild: Guild) = conversation {

        val config = discord.getInjectionObject<Configuration>()
        val persistenceService = discord.getInjectionObject<PersistenceService>()

        val adminRole = blockingPromptUntil(
            SentenceArg,
            { "What's the admin role name?" },
            { role -> guild.getRoleByIdOrName(role) != null },
            { "That role doesn't exist on ${guild.name}."}
        )

        val staffRole = blockingPromptUntil(
            SentenceArg,
            { "What's the staff role name?" },
            { role -> guild.getRoleByIdOrName(role) != null },
            { "That role doesn't exist on ${guild.name}."}
        )

        val loggingChannel = blockingPromptUntil(
            TextChannelArg,
            { "What's the logging channel ID?" },
            { channel -> guild.textChannels.contains(channel) },
            { "That channel doesn't exist on ${guild.name}."}
        )

        val historyChannel = blockingPromptUntil(
            TextChannelArg,
            { "What's the history channel ID?" },
            { channel -> guild.textChannels.contains(channel) },
            { "That channel doesn't exist on ${guild.name}."}
        )

        val messageCacheAmt = blockingPromptUntil(
            IntegerArg,
            { "How many messages would you like to cache?" },
            { cacheAmt -> cacheAmt > 0 || cacheAmt <= config!!.maxCacheAmount },
            { "You can only cache between 1 and ${config!!.maxCacheAmount}" }
        )

        config!!.guildConfigurations.add(GuildConfiguration(
            guildId = guild.id,
            adminRole = adminRole,
            staffRole = staffRole,
            loggingChannel = loggingChannel.id,
            historyChannel = historyChannel.id,
            messageCacheAmount = messageCacheAmt
        ))

        persistenceService!!.save(config)
    }
}