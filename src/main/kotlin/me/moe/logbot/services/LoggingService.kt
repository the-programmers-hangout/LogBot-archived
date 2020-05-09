package me.moe.logbot.services

import me.aberrantfox.kjdautils.api.annotation.Service
import me.aberrantfox.kjdautils.api.dsl.embed
import me.aberrantfox.kjdautils.extensions.jda.descriptor
import me.moe.logbot.data.Configuration
import me.moe.logbot.extensions.*
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.events.GatewayPingEvent
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateNameEvent
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateSlowmodeEvent
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateTopicEvent
import net.dv8tion.jda.api.events.emote.EmoteAddedEvent
import net.dv8tion.jda.api.events.emote.EmoteRemovedEvent
import net.dv8tion.jda.api.events.emote.update.EmoteUpdateNameEvent
import net.dv8tion.jda.api.events.guild.GuildBanEvent
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent
import net.dv8tion.jda.api.events.guild.member.*
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent
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
            color = successColor
            image = event.emote.imageUrl
        }
    }

    fun buildEmoteDeletedEmbed(event: EmoteRemovedEvent) = withLog(event.guild) {
        embed {
            title = "Emote Removed"
            description = "The emote `${event.emote.name}` has been removed"
            color = failureColor
            image = event.emote.imageUrl
        }
    }

    fun buildEmoteRenameEmbed(event: EmoteUpdateNameEvent) = withLog(event.guild) {
        embed {
            title = "Emote Renamed"
            description = "The emote `${event.oldName}` has been renamed to `${event.newName}`"
            color = infoColor
            image = event.emote.imageUrl
        }
    }

    /*

        Joins / Leaves

     */

    fun buildMemberJoinEmbed(event: GuildMemberJoinEvent) = withLog(event.guild) {
        embed {
            title = "User Joined"
            color = successColor

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

    fun buildMemberLeaveEmbed(event: GuildMemberRemoveEvent) = withLog(event.guild) {
        embed {
            title = "User Left"
            color = failureColor

            field {
                name = "User"
                value = event.user.verboseDescriptor()
            }
        }
    }

    /*

        Bans/Unbans

     */

    fun buildMemberBanEmbed(event: GuildMemberRemoveEvent) = withLog(event.guild) {
        embed {
            title = "User Banned"
            color = failureColor

            field {
                name = "User"
                value = event.user.verboseDescriptor()
            }
        }
    }

    fun buildMemberUnbanEmbed(event: GuildUnbanEvent) = withLog(event.guild) {
        embed {
            title = "User Unbanned"
            color = successColor

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
            color = successColor

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
            color = failureColor

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
            color = failureColor

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
            description = "[Jump to message](${event.message.jumpUrl})"
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

    /*

        Reactions

     */
    fun buildReactionAddedEvent(event: GuildMessageReactionAddEvent) = withLog(event.guild) {

        val message = event.channel.retrieveMessageById(event.messageId).complete()
        embed {
            title = "Reaction added"
            if (message != null)
                description = "[Jump to](${message.jumpUrl})"
            color = successColor
            thumbnail = if(event.reactionEmote.isEmoji) null else event.reactionEmote.emote.imageUrl

            field {
                name = "User"
                value = event.member.user.verboseDescriptor()
            }

            field {
                name = "Emote"
                value = event.reactionEmote.name
            }

        }
    }

    fun buildReactionRemovedEvent(event: GuildMessageReactionRemoveEvent) = withLog(event.guild) {

        val message = event.channel.retrieveMessageById(event.messageId).complete()
        embed {
            title = "Reaction removed"
            if (message != null)
                description = "[Jump to message](${message.jumpUrl})"
            color = failureColor
            thumbnail = if(event.reactionEmote.isEmoji) null else event.reactionEmote.emote.imageUrl

            if (event.member != null) {
                field {
                    name = "User"
                    value = event.member!!.user.verboseDescriptor()
                }
            }


            field {
                name = "Emote"
                value = event.reactionEmote.name
            }

        }
    }


    /*

        Voice

     */

    fun buildVoiceJoinEmbed (event: GuildVoiceJoinEvent) = withLog(event.guild) {
        embed {
            title = "Voice join"
            color = successColor

            field {
                name = "User"
                value = event.member.user.verboseDescriptor()
            }

            field {
                name = "Voice channel"
                value = event.channelJoined.descriptor()
            }
        }
    }

    fun buildVoiceLeaveEmbed (event: GuildVoiceLeaveEvent) = withLog(event.guild) {
        embed {
            title = "Voice leave"
            color = failureColor

            field {
                name = "User"
                value = event.member.user.verboseDescriptor()
            }

            field {
                name = "Voice channel"
                value = event.channelLeft.descriptor()
            }
        }
    }

    fun buildVoiceMoveEmbed (event: GuildVoiceMoveEvent) = withLog(event.guild) {
        embed {
            title = "Voice move"
            color = infoColor

            field {
                name = "User"
                value = event.member.user.verboseDescriptor()
            }

            field {
                name = "Old channel"
                value = event.channelLeft.descriptor()
            }

            field {
                name = "New channel"
                value = event.channelJoined.descriptor()
            }
        }
    }


    /*

        Channels

     */

    fun buildSlowmodeUpdateEmbed(event: TextChannelUpdateSlowmodeEvent) = withLog(event.guild) {
        embed {
            title = "Slowmode Updated"
            color = infoColor

            field {
                name = "Channel"
                value = event.channel.descriptor()
            }
            field {
                name = "Old slowmode"
                value = event.oldSlowmode.toTimeStringFromSec()
            }

            field {
                name = "New slowmode"
                value = event.newSlowmode.toTimeStringFromSec()
            }

        }
    }

    fun buildChannelCreateEmbed(event: TextChannelCreateEvent) = withLog(event.guild) {
        embed {
            title = "Channel created"
            color = successColor

            field {
                name = "Channel"
                value = event.channel.descriptor()
            }
        }
    }

    fun buildChannelDeleteEmbed(event: TextChannelDeleteEvent) = withLog(event.guild) {
        embed {
            title = "Channel deleted"
            color = failureColor

            field {
                name = "Channel"
                value = event.channel.descriptor()
            }
        }
    }

    fun buildChannelUpdateNameEmbed(event: TextChannelUpdateNameEvent) = withLog(event.guild) {
        embed {
            title = "Update channel name"
            color = infoColor

            field {
                name = "Channel"
                value = event.channel.descriptor()
            }

            field {
                name = "Old name"
                value = event.oldName
            }

            field {
                name = "New name"
                value = event.newName
            }
        }
    }

    fun buildChannelUpdateTopicEmbed(event: TextChannelUpdateTopicEvent) = withLog(event.guild) {
        embed {
            title = "Channel topic updated"
            color = infoColor

            if (event.oldTopic != null)
                createContinuableField("Old topic", event.oldTopic!!)

            if (event.newTopic != null)
                createContinuableField("New topic", event.newTopic!!)
        }
    }




    private fun getLogConfig(guildId: String) = config.getGuildConfig(guildId)!!.loggingChannel
    private fun getHistoryConfig(guildId: String) = config.getGuildConfig(guildId)!!.historyChannel
    private fun log(guild: Guild, logChannelId: String, message: MessageEmbed) = logChannelId.takeIf { it.isNotEmpty() }?.idToTextChannel(guild)
            ?.sendMessage(message)?.queue()

    private fun String.idToTextChannel(guild: Guild) = guild.jda.getTextChannelById(this)

}