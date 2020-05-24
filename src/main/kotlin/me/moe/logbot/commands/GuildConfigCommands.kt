package me.moe.logbot.commands

import me.aberrantfox.kjdautils.api.annotation.CommandSet
import me.aberrantfox.kjdautils.api.dsl.command.commands
import me.aberrantfox.kjdautils.extensions.jda.getRoleByName
import me.aberrantfox.kjdautils.internal.arguments.IntegerArg
import me.aberrantfox.kjdautils.internal.arguments.RoleArg
import me.aberrantfox.kjdautils.internal.arguments.TextChannelArg
import me.aberrantfox.kjdautils.internal.services.PersistenceService
import me.moe.logbot.data.Configuration
import me.moe.logbot.extensions.descriptor
import me.moe.logbot.extensions.requiredPermissionLevel
import me.moe.logbot.services.CacheService
import me.moe.logbot.services.Permission
import me.moe.logbot.util.embeds.GuildSetupCommandsEmbedUtils.Companion.buildGuildConfigEmbed
import me.moe.logbot.util.embeds.UtilEmbeds.Companion.buildErrorEmbed
import me.moe.logbot.util.embeds.UtilEmbeds.Companion.buildGuildNotSetupEmbed
import me.moe.logbot.util.embeds.UtilEmbeds.Companion.buildSuccessEmbed

@CommandSet("Guild Config")
fun guildConfigCommands(configuration: Configuration, persistenceService: PersistenceService,
                        cacheService: CacheService) = commands {

    command("ViewConfiguration") {
        requiredPermissionLevel = Permission.Staff

        execute {
            val config = configuration.getGuildConfig(it.guild!!.id)
                    ?: return@execute it.respond(buildGuildNotSetupEmbed())

            val guild = it.discord.jda.getGuildById(config.guildId)?.descriptor() ?: "The guildID is either not set or is invalid"

            val adminRole = it.guild!!.getRoleByName(config.adminRole)?.descriptor()
                    ?: "The admin role is either not set or is invalid"

            val staffRole = it.guild!!.getRoleByName(config.staffRole)?.descriptor()
                    ?: "The staff role is either not set or is invalid"

            val loggingChannel = it.guild!!.getTextChannelById(config.loggingChannel)?.descriptor()
                    ?: "The logging channel is either not set or is invalid."

            val historyChannel = it.guild!!.getTextChannelById(config.historyChannel)?.descriptor()
                    ?: "The history channel is either not set or is invalid."

            val cacheAmt = config.messageCacheAmount.toString()

            it.respond(buildGuildConfigEmbed(guild, adminRole, staffRole, loggingChannel, historyChannel, cacheAmt))

        }
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