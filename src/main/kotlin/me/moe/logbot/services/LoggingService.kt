package me.moe.logbot.services

import me.aberrantfox.kjdautils.api.annotation.Service
import me.aberrantfox.kjdautils.api.dsl.embed
import me.aberrantfox.kjdautils.extensions.jda.descriptor
import me.moe.logbot.data.Configuration
import me.moe.logbot.extensions.createContinuableField
import me.moe.logbot.extensions.descriptor
import me.moe.logbot.extensions.verboseDescriptor
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.events.emote.EmoteAddedEvent
import net.dv8tion.jda.api.events.emote.EmoteRemovedEvent
import net.dv8tion.jda.api.events.emote.update.EmoteUpdateNameEvent
import net.dv8tion.jda.api.events.guild.GuildBanEvent
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent
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

    private fun withHistory(guild: Guild, f: () -> MessageEmbed) =
            getHistoryConfig(guild.id).apply {
                log(guild, getHistoryConfig(guild.id), f())
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

    /*

        Nicknames

     */

    fun buildNicknameChangeEmbed(event: GuildMemberUpdateNicknameEvent) = withLog(event.guild) {
        embed {
            title = "Nickname ${if (event.newNickname == null) "Reset" else "Updated"}"
            color = Color.CYAN

            field {
                name = "User"
                value = event.user.verboseDescriptor()
            }

            if (event.oldNickname != null)
                field {
                    name = "Old Nickname"
                    value = event.oldNickname
                }

            if (event.newNickname != null)
                field {
                    name = "New Nickname"
                    value = event.newNickname
                }
        }
    }

    /*

        Message Tracking

     */

    fun buildMessageDeletedEmbed(event: GuildMessageDeleteEvent, deletedMessage: Message) = withHistory(event.guild) {
        embed {
            title = "Message Deleted"
            color = Color.RED

            field {
                name = "User"
                value = deletedMessage.author.verboseDescriptor()
            }

            field {
                name = "Channel"
                value = deletedMessage.textChannel.descriptor()
            }

            createContinuableField("Message Content", deletedMessage.contentRaw)

            if (deletedMessage.attachments.isNotEmpty()) {
                field {
                    name = "Files"
                    value = deletedMessage.attachments.joinToString(separator = "\n") { it.url }
                }
            }
        }
    }

    fun buildMessageEditedEmbed(event: GuildMessageUpdateEvent, oldMessage: Message) = withHistory(event.guild) {
        embed {
            title = "Message Edited"
            description = event.message.jumpUrl
            color = Color.ORANGE

            field {
                name = "User"
                value = event.author.verboseDescriptor()
            }

            field {
                name = "Channel"
                value = oldMessage.textChannel.descriptor()
            }

            createContinuableField("Old", oldMessage.contentRaw)
            createContinuableField("New", event.message.contentRaw)
        }
    }

    private fun getLogConfig(guildId: String) = config.getGuildConfig(guildId)!!.loggingChannel
    private fun getHistoryConfig(guildId: String) = config.getGuildConfig(guildId)!!.historyChannel
    private fun log(guild: Guild, logChannelId: String, message: MessageEmbed) = logChannelId.takeIf { it.isNotEmpty() }?.idToTextChannel(guild)
            ?.sendMessage(message)?.queue()

    private fun String.idToTextChannel(guild: Guild) = guild.jda.getTextChannelById(this)

}