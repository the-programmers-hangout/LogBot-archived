package me.moe.logbot.commands

import me.aberrantfox.kjdautils.api.annotation.CommandSet
import me.aberrantfox.kjdautils.api.dsl.command.commands
import me.aberrantfox.kjdautils.internal.arguments.IntegerArg
import me.aberrantfox.kjdautils.internal.arguments.RoleArg
import me.aberrantfox.kjdautils.internal.arguments.TextChannelArg
import me.aberrantfox.kjdautils.internal.services.ConversationService
import me.aberrantfox.kjdautils.internal.services.PersistenceService
import me.moe.logbot.conversations.GuildSetupConversation
import me.moe.logbot.data.Configuration
import me.moe.logbot.extensions.requiredPermissionLevel
import me.moe.logbot.locale.messages
import me.moe.logbot.services.CacheService
import me.moe.logbot.services.Permission
import me.moe.logbot.util.embeds.GuildSetupCommandsEmbedUtils.Companion.buildGuildSetupEmbed
import me.moe.logbot.util.embeds.UtilEmbeds.Companion.buildErrorEmbed
import me.moe.logbot.util.embeds.UtilEmbeds.Companion.buildGuildNotSetupEmbed
import me.moe.logbot.util.embeds.UtilEmbeds.Companion.buildSuccessEmbed

@CommandSet("Guild Config")
fun guildConfigCommands(configuration: Configuration, persistenceService: PersistenceService,
                        conversationService: ConversationService, cacheService: CacheService) = commands {
    command("Setup") {
        requiredPermissionLevel = Permission.GuildOwner

        execute {commandEvent ->

            if (configuration.hasGuildConfig(commandEvent.guild!!.id)) {
                commandEvent.respond(buildErrorEmbed(messages.errors.GUILD_ALREADY_SETUP))

                return@execute
            }

            commandEvent.author.openPrivateChannel().queue {
                it.sendMessage(buildGuildSetupEmbed(commandEvent.guild!!)).queue({
                    conversationService.startConversation<GuildSetupConversation>(commandEvent.author, commandEvent.guild!!)
                }, {
                    commandEvent.respond(buildErrorEmbed(messages.errors.DMS_CLOSED))
                })
            }
        }
    }

    command("ViewConfiguration") {

    }

    command("AdminRole") {
        requiredPermissionLevel = Permission.GuildOwner

        execute(RoleArg) {
            val newAdmin = it.args.first

            val config = configuration.getGuildConfig(it.guild!!.id)
                    ?: return@execute it.respond(buildGuildNotSetupEmbed())

            config.adminRole = newAdmin.name
            persistenceService.save(configuration)

            it.respond(buildSuccessEmbed("Admin role set to ${newAdmin.name}"))
        }
    }

    command("StaffRole") {
        requiredPermissionLevel = Permission.GuildOwner

        execute(RoleArg) {
            val newStaff = it.args.first

            val config = configuration.getGuildConfig(it.guild!!.id)
                    ?: return@execute it.respond(buildGuildNotSetupEmbed())

            config.staffRole = newStaff.name
            persistenceService.save(configuration)

            it.respond(buildSuccessEmbed("Staff role set to ${newStaff.name}"))
        }
    }

    command("LoggingChannel") {
        requiredPermissionLevel = Permission.Administrator
        execute(TextChannelArg) {
            val channel = it.args.first

            val config = configuration.getGuildConfig(it.guild!!.id)
                    ?: return@execute it.respond(buildGuildNotSetupEmbed())

            config.loggingChannel = channel.id
            persistenceService.save(configuration)

            it.respond(buildSuccessEmbed("Logging channel set to ${channel.name}"))
        }
    }

    command("HistoryChannel") {
        requiredPermissionLevel = Permission.Administrator
        execute(TextChannelArg) {
            val channel = it.args.first

            val config = configuration.getGuildConfig(it.guild!!.id)
                ?: return@execute it.respond(buildGuildNotSetupEmbed())

            config.historyChannel = channel.id
            persistenceService.save(configuration)

            it.respond(buildSuccessEmbed("History channel set to ${channel.name}"))
        }
    }

    command("MessageCacheAmount") {
        requiredPermissionLevel = Permission.Administrator

        execute(IntegerArg) {
            val cacheAmt = it.args.first

            val config = configuration.getGuildConfig(it.guild!!.id)
                    ?: return@execute it.respond(buildGuildNotSetupEmbed())

            if (cacheAmt <= 0 || cacheAmt > configuration.maxCacheAmount) {
                it.respond(buildErrorEmbed("You can only cache between 1 " +
                        "and ${configuration.maxCacheAmount} message(s)"))

                return@execute
            }

            cacheService.setCacheLimit(it.guild!!, cacheAmt)

            config.messageCacheAmount = cacheAmt
            persistenceService.save(configuration)

            it.respond(buildSuccessEmbed("Now caching $cacheAmt message(s)"))
        }
    }
}