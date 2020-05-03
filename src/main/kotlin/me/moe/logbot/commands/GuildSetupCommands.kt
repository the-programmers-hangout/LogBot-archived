package me.moe.logbot.commands

import me.aberrantfox.kjdautils.api.annotation.CommandSet
import me.aberrantfox.kjdautils.api.dsl.command.commands
import me.aberrantfox.kjdautils.internal.services.ConversationService
import me.aberrantfox.kjdautils.internal.services.PersistenceService
import me.moe.logbot.conversations.GuildSetupConversation
import me.moe.logbot.data.Configuration
import me.moe.logbot.extensions.requiredPermissionLevel
import me.moe.logbot.locale.messages
import me.moe.logbot.services.Permission
import me.moe.logbot.util.embeds.GuildSetupCommandsEmbedUtils.Companion.buildGuildSetupEmbed

@CommandSet("Guild Setup")
fun guildSetupCommands(configuration: Configuration,
                     persistenceService: PersistenceService, conversationService: ConversationService) = commands {
    command("Setup") {
        requiredPermissionLevel = Permission.GuildOwner

        execute {commandEvent ->

            if (configuration.hasGuildConfig(commandEvent.guild!!.id)) {
                commandEvent.respond(messages.errors.GUILD_ALREADY_SETUP)

                return@execute
            }

            commandEvent.author.openPrivateChannel().queue {
                it.sendMessage(buildGuildSetupEmbed(commandEvent.guild!!)).queue({
                    conversationService.startConversation<GuildSetupConversation>(commandEvent.author, commandEvent.guild!!)
                }, {
                    commandEvent.respond(messages.errors.DMS_CLOSED)
                })
            }
        }
    }
}