package me.moe.logbot.commands

import me.aberrantfox.kjdautils.api.annotation.CommandSet
import me.aberrantfox.kjdautils.api.dsl.command.commands
import me.aberrantfox.kjdautils.extensions.jda.getRoleByName
import me.aberrantfox.kjdautils.internal.arguments.RoleArg
import me.aberrantfox.kjdautils.internal.services.PersistenceService
import me.moe.logbot.data.Configuration
import me.moe.logbot.extensions.descriptor
import me.moe.logbot.extensions.requiredPermissionLevel
import me.moe.logbot.locale.messages
import me.moe.logbot.services.Permission
import me.moe.logbot.util.embeds.RoleConfigCommandsEmbedUtils.Companion.buildAlreadyIgnoredEmbed
import me.moe.logbot.util.embeds.RoleConfigCommandsEmbedUtils.Companion.buildIgnoredEmbed
import me.moe.logbot.util.embeds.RoleConfigCommandsEmbedUtils.Companion.buildIgnoredRolesEmbed
import me.moe.logbot.util.embeds.RoleConfigCommandsEmbedUtils.Companion.buildNotIgnoredEmbed
import me.moe.logbot.util.embeds.RoleConfigCommandsEmbedUtils.Companion.buildUnignoredEmbed


@CommandSet("RoleConfiguration")
fun roleConfigCommands(configuration: Configuration,
                          persistenceService: PersistenceService) = commands {

    command("IgnoredRoles") {
        requiredPermissionLevel = Permission.Staff
        description = messages.descriptions.IGNORED_ROLES
        execute {
            it.container.commands

            val config = configuration.getGuildConfig(it.guild!!.id)
                    ?: return@execute it.respond(messages.errors.GUILD_NOT_SETUP)

            val test = config.ignoreRoleNames.map {ignoredRole ->
                val role = it.guild!!.getRoleByName(ignoredRole)
                role?.descriptor() ?: "$ignoredRole (invalid role)"
            }

            it.respond(buildIgnoredRolesEmbed(test))
        }
    }

    command("IgnoreRole") {
        requiredPermissionLevel = Permission.Administrator
        description = messages.descriptions.IGNORE_ROLE
        execute(RoleArg) {
            val role = it.args.first
            val config = configuration.getGuildConfig(it.guild!!.id)
                    ?: return@execute it.respond(messages.errors.GUILD_NOT_SETUP)

            if (config.ignoreRoleNames.contains(role.name)) {
                it.respond(buildAlreadyIgnoredEmbed(role))

                return@execute
            }

            config.ignoreRoleNames.add(role.name)
            persistenceService.save(configuration)

            it.respond(buildIgnoredEmbed(role))
        }
    }

    command("UnignoreRole") {
        requiredPermissionLevel = Permission.Administrator
        description = messages.descriptions.UNIGNORE_ROLE
        execute(RoleArg) {
            val role = it.args.first
            val config = configuration.getGuildConfig(it.guild!!.id)
                    ?: return@execute it.respond(messages.errors.GUILD_NOT_SETUP)

            if (!config.ignoreRoleNames.contains(role.name)) {
                it.respond(buildNotIgnoredEmbed(role))

                return@execute
            }

            config.ignoreRoleNames.remove(role.name)
            persistenceService.save(configuration)

            it.respond(buildUnignoredEmbed(role))
        }
    }

}