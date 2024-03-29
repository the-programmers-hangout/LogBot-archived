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
import me.moe.logbot.locale.messages
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
        description = messages.descriptions.VIEW_CONFIGURATION

        execute {
            val config = configuration.getGuildConfig(it.guild!!.id)
                    ?: return@execute it.respond(buildGuildNotSetupEmbed())

            val guild = it.discord.jda.getGuildById(config.guildId)?.descriptor()
                    ?: "The guildID is either not set or is invalid"

            val adminRole = it.guild!!.getRoleById(config.adminRole)?.descriptor()
                    ?: "The admin role is either not set or is invalid"

            val staffRole = it.guild!!.getRoleById(config.staffRole)?.descriptor()
                    ?: "The staff role is either not set or is invalid"

            val loggingChannel = it.guild!!.getTextChannelById(config.loggingChannel)?.descriptor()
                    ?: "The logging channel is either not set or is invalid."

            val historyChannel = it.guild!!.getTextChannelById(config.historyChannel)?.descriptor()
                    ?: "The history channel is either not set or is invalid."

            val cacheAmt = configuration.cacheAmount

            it.respond(buildGuildConfigEmbed(guild, adminRole, staffRole, loggingChannel, historyChannel, cacheAmt))

        }
    }

    command("AdminRole") {
        requiredPermissionLevel = Permission.GuildOwner
        description = messages.descriptions.ADMIN_ROLE

        execute(RoleArg) {
            val newAdmin = it.args.first

            val config = configuration.getGuildConfig(it.guild!!.id)
                    ?: return@execute it.respond(buildGuildNotSetupEmbed())

            config.adminRole = newAdmin.id
            persistenceService.save(configuration)

            it.respond(buildSuccessEmbed("Admin role set to ${newAdmin.name}"))
        }
    }

    command("StaffRole") {
        requiredPermissionLevel = Permission.GuildOwner
        description = messages.descriptions.STAFF_ROLE

        execute(RoleArg) {
            val newStaff = it.args.first

            val config = configuration.getGuildConfig(it.guild!!.id)
                    ?: return@execute it.respond(buildGuildNotSetupEmbed())

            config.staffRole = newStaff.id
            persistenceService.save(configuration)

            it.respond(buildSuccessEmbed("Staff role set to ${newStaff.name}"))
        }
    }

    command("LoggingChannel") {
        requiredPermissionLevel = Permission.Administrator
        description = messages.descriptions.LOGGING_CHANNEL

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
        description = messages.descriptions.HISTORY_CHANNEL

        execute(TextChannelArg) {
            val channel = it.args.first

            val config = configuration.getGuildConfig(it.guild!!.id)
                ?: return@execute it.respond(buildGuildNotSetupEmbed())

            config.historyChannel = channel.id
            persistenceService.save(configuration)

            it.respond(buildSuccessEmbed("History channel set to ${channel.name}"))
        }
    }
}