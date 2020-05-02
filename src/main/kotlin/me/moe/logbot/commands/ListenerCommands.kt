package me.moe.logbot.commands

import me.aberrantfox.kjdautils.api.annotation.CommandSet
import me.aberrantfox.kjdautils.api.dsl.command.*
import me.aberrantfox.kjdautils.internal.arguments.BooleanArg
import me.aberrantfox.kjdautils.internal.arguments.ChoiceArg
import me.aberrantfox.kjdautils.internal.services.PersistenceService
import me.moe.logbot.data.Configuration
import me.moe.logbot.extensions.requiredPermissionLevel
import me.moe.logbot.locale.messages
import me.moe.logbot.services.Permission
import me.moe.logbot.util.embeds.ListenerCommandsEmbedUtils.Companion.buildLoggerStatusEmbed
import me.moe.logbot.util.embeds.ListenerCommandsEmbedUtils.Companion.buildLoggerToggledEmbed

@CommandSet("Listeners")
fun listenerCommands(configuration: Configuration,
                          persistenceService: PersistenceService) = commands {

    command("ListeningStatus") {
        requiredPermissionLevel = Permission.Staff
        description = messages.descriptions.LISTENER_STATUS
        execute {
            val config = configuration.getGuildConfig(it.guild!!.id)
                    ?: return@execute it.respond(messages.errors.GUILD_NOT_SETUP)

            val enabledLoggers = mutableListOf<String>()
            val disabledLoggers = mutableListOf<String>()

            // could be cleaner but it works™ (KISS > DRY)
            if (config.trackEmotes) enabledLoggers.add("Emotes") else disabledLoggers.add("Emotes")
            if (config.trackMembers) enabledLoggers.add("Member leaves/joins") else disabledLoggers.add("Member leaves/joins")
            if (config.trackBans) enabledLoggers.add("Bans/Unbans") else disabledLoggers.add("Bans/Unbans")
            if (config.trackRoles) enabledLoggers.add("Roles") else disabledLoggers.add("Roles")
            if (config.trackNicknames) enabledLoggers.add("Nicknames") else disabledLoggers.add("Nicknames")
            if (config.trackMessages) enabledLoggers.add("Messages") else disabledLoggers.add("Messages")
            if (config.trackReactions) enabledLoggers.add("Reactions") else disabledLoggers.add("Reactions")
            if (config.trackVoice) enabledLoggers.add("Voice") else disabledLoggers.add("Voice")
            if (config.trackChannels) enabledLoggers.add("Channels") else disabledLoggers.add("Channels")
            if (config.trackCategories) enabledLoggers.add("Categories") else disabledLoggers.add("Categories")

            it.respond(buildLoggerStatusEmbed(enabledLoggers, disabledLoggers))

        }
    }

    command("ToggleListener") {
        requiredPermissionLevel = Permission.Staff
        description = messages.descriptions.TOGGLE_LISTENER
        execute(
            ChoiceArg(name=listOfLoggers.joinToString(separator = " | "), choices = *listOfLoggers.toTypedArray()),
            BooleanArg("On or Off", "On", "Off")) {

            val logger = it.args.component1()
            val isOn = it.args.component2()

            val config = configuration.getGuildConfig(it.guild!!.id)
                ?: return@execute it.respond(messages.errors.GUILD_NOT_SETUP)

            // could probably be cleaner but... it just works™ (KISS > DRY)
            when (logger) {
                "emotes" -> config.trackEmotes = isOn
                "members" -> config.trackMembers = isOn
                "bans" -> config.trackBans = isOn
                "roles" -> config.trackRoles = isOn
                "nicknames" -> config.trackNicknames = isOn
                "messages" -> config.trackMessages = isOn
                "reactions" -> config.trackReactions = isOn
                "voice" -> config.trackVoice = isOn
                "channels" -> config.trackChannels = isOn
                "categories" -> config.trackCategories = isOn
            }

            persistenceService.save(configuration)
            it.respond(buildLoggerToggledEmbed(logger, isOn))
        }
    }
}


private val listOfLoggers = listOf("emotes", "members", "bans", "roles", "nicknames", "messages", "reactions",
    "voice", "channels", "categories")