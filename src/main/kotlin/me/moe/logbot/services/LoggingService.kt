package me.moe.logbot.services

import me.aberrantfox.kjdautils.api.annotation.Service
import me.aberrantfox.kjdautils.api.dsl.embed
import me.aberrantfox.kjdautils.extensions.jda.descriptor
import me.moe.logbot.data.Configuration
import me.moe.logbot.extensions.descriptor
import me.moe.logbot.extensions.verboseDescriptor
import net.dv8tion.jda.api.entities.Emote
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.emote.EmoteAddedEvent
import net.dv8tion.jda.api.events.emote.EmoteRemovedEvent
import net.dv8tion.jda.api.events.emote.update.EmoteUpdateNameEvent
import net.dv8tion.jda.api.events.guild.GuildBanEvent
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent
import java.awt.Color
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class LoggingService(private val config: Configuration) {

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("E, d L u H:m:sa")
    private fun withLog(guild: Guild, f: () -> MessageEmbed) =
            getLogConfig(guild.id).apply {
                log(guild, getLogConfig(guild.id), f())
            }


    /*

        Emotes

     */
    fun buildEmoteAddedEmbed(event: EmoteAddedEvent) = withLog(event.guild) {
        embed {
            title = "Emote Added"
            description = "The emote `${event.emote.name}` has been added"
            color = Color.RED
            image = event.emote.imageUrl
        }
    }

    fun buildEmoteDeletedEmbed(event: EmoteRemovedEvent) = withLog(event.guild) {
        embed {
            title = "Emote Removed"
            description = "The emote `${event.emote.name}` has been removed"
            color = Color.RED
            image = event.emote.imageUrl
        }
    }

    fun buildEmoteRenameEmbed(event: EmoteUpdateNameEvent) = withLog(event.guild) {
        embed {
            title = "Emote Renamed"
            description = "The emote `${event.oldName}` has been renamed to `${event.newName}`"
            color = Color.WHITE
            image = event.emote.imageUrl
        }
    }

    /*

        Joins / Leaves

     */

    fun buildMemberJoinEmbed(event: GuildMemberJoinEvent) = withLog(event.guild) {
        embed {
            title = "User Joined"
            color = Color.GREEN

            field {
                name = "User"
                value = event.user.verboseDescriptor()
            }
            field {
                name = "Created at"
                value = "${event.user.timeCreated.format(dateTimeFormatter)} "
            }
        }
    }

    fun buildMemberLeaveEmbed(event: GuildMemberLeaveEvent) = withLog(event.guild) {
        embed {
            title = "User Left"
            color = Color.RED

            field {
                name = "User"
                value = event.user.verboseDescriptor()
            }
        }
    }

    /*

        Bans/Unbans

     */

    fun buildMemberBanEmbed(event: GuildBanEvent) = withLog(event.guild) {
        embed {
            title = "User Banned"
            color = Color.RED

            field {
                name = "User"
                value = event.user.verboseDescriptor()
            }
        }
    }

    fun buildMemberUnbanEmbed(event: GuildUnbanEvent) = withLog(event.guild) {
        embed {
            title = "User Unbanned"
            color = Color.GREEN

            field {
                name = "User"
                value = event.user.verboseDescriptor()
            }
        }
    }

    /*

        Roles

     */

    fun buildRoleAddEmbed(event: GuildMemberRoleAddEvent) = withLog(event.guild) {
        val roleOrRoles = if (event.roles.size > 1) "Roles" else "Role"

        embed {
            title = "$roleOrRoles Added To User"
            color = Color.GREEN

            field {
                name = "User"
                value = event.user.verboseDescriptor()
            }

            field {
                name = roleOrRoles
                value = event.roles.joinToString(separator = "\n") { it.descriptor() }
            }
        }
    }

    fun buildRoleRemoveEmbed(event: GuildMemberRoleRemoveEvent) = withLog(event.guild) {
        val roleOrRoles = if (event.roles.size > 1) "Roles" else "Role"

        embed {
            title = "$roleOrRoles Removed From User"
            color = Color.RED

            field {
                name = "User"
                value = event.user.verboseDescriptor()
            }

            field {
                name = roleOrRoles
                value = event.roles.joinToString(separator = "\n") { it.descriptor() }
            }
        }
    }

    private fun getLogConfig(guildId: String) = config.getGuildConfig(guildId)!!.loggingChannel
    private fun log(guild: Guild, logChannelId: String, message: MessageEmbed) = logChannelId.takeIf { it.isNotEmpty() }?.idToTextChannel(guild)
            ?.sendMessage(message)?.queue()

    private fun String.idToTextChannel(guild: Guild) = guild.jda.getTextChannelById(this)

}