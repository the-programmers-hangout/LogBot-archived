package me.moe.logbot.commands

import me.aberrantfox.kjdautils.api.annotation.CommandSet
import me.aberrantfox.kjdautils.api.dsl.command.commands
import me.aberrantfox.kjdautils.internal.arguments.RoleArg
import me.aberrantfox.kjdautils.internal.services.PersistenceService
import me.moe.logbot.data.Configuration
import me.moe.logbot.extensions.requiredPermissionLevel
import me.moe.logbot.locale.messages
import me.moe.logbot.services.Permission
import me.moe.logbot.util.embeds.ListenerCommandsEmbedUtils.Companion.buildIgnoredRolesEmbed


@CommandSet("RoleConfiguration")
fun roleConfigCommands(configuration: Configuration,
                          persistenceService: PersistenceService) = commands {

    command("IgnoredRoles") {
        requiredPermissionLevel = Permission.Staff
        execute {
            val config = configuration.getGuildConfig(it.guild!!.id)
                    ?: return@execute it.respond(messages.errors.GUILD_NOT_SETUP)

            it.respond(buildIgnoredRolesEmbed(config.ignoreRoleNames))
        }
    }

    command("IgnoreRole") {
        requiredPermissionLevel = Permission.Administrator
        execute(RoleArg) {
            val role = it.args.first
            val config = configuration.getGuildConfig(it.guild!!.id)
                    ?: return@execute it.respond(messages.errors.GUILD_NOT_SETUP)

            if (config.ignoreRoleNames.contains(role.name)) {
                it.respond("${role.name} is already ignored")

                return@execute
            }

            config.ignoreRoleNames.add(role.name)
            persistenceService.save(configuration)

            it.respond("ignoring ${role.name}")
        }
    }

    command("UnignoreRole") {
        requiredPermissionLevel = Permission.Administrator
        execute(RoleArg) {
            val role = it.args.first
            val config = configuration.getGuildConfig(it.guild!!.id)
                    ?: return@execute it.respond(messages.errors.GUILD_NOT_SETUP)

            if (!config.ignoreRoleNames.contains(role.name)) {
                it.respond("${role.name} is not ignored")

                return@execute
            }

            config.ignoreRoleNames.remove(role.name)
            persistenceService.save(configuration)

            it.respond("ignoring ${role.name}")
        }
    }

}