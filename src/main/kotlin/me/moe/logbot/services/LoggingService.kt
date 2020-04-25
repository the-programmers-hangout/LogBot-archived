package me.moe.logbot.services

import me.aberrantfox.kjdautils.api.annotation.Service
import me.aberrantfox.kjdautils.api.dsl.embed
import me.aberrantfox.kjdautils.extensions.jda.descriptor
import me.moe.logbot.data.Configuration
import net.dv8tion.jda.api.entities.Emote
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.emote.update.EmoteUpdateNameEvent
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
    fun buildEmoteAddedEmbed(guild: Guild, emote: Emote) = withLog(guild) {
        embed {
            title = "Emote Added"
            description = "The emote `${emote.name}` has been added"
            color = Color.RED
            image = emote.imageUrl
        }
    }

    fun buildEmoteDeletedEmbed(guild: Guild, emote: Emote) = withLog(guild) {
        embed {
            title = "Emote Removed"
            description = "The emote `${emote.name}` has been removed"
            color = Color.RED
            image = emote.imageUrl
        }
    }

    fun buildEmoteRenameEmbed(guild: Guild, event: EmoteUpdateNameEvent) = withLog(guild) {
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

    fun buildMemberJoinEmbed(guild: Guild, user: User) = withLog(guild) {
        embed {
            title = "User joined"
            color = Color.GREEN

            field {
                name = "User"
                value = "**${user.asMention} :: ${user.descriptor()}**"
            }
            field {
                name = "Created at"
                value = "${user.timeCreated.format(dateTimeFormatter)} "
            }
        }
    }

    fun buildMemberLeaveEmbed(guild: Guild, user: User) = withLog(guild) {
        embed {
            title = "User left"
            color = Color.RED

            field {
                name = "User"
                value = "**${user.asMention} :: ${user.descriptor()}**"
            }
        }
    }

    /*

        Bans/Unbans

     */

    fun buildMemberBanEmbed(guild: Guild, user: User) = withLog(guild) {
        embed {
            title = "User Banned"
            color = Color.RED

            field {
                name = "User"
                value = "**${user.asMention} :: ${user.descriptor()}**"
            }
        }
    }

    fun buildMemberUnbanEmbed(guild: Guild, user: User) = withLog(guild) {
        embed {
            title = "User Unbanned"
            color = Color.GREEN

            field {
                name = "User"
                value = "**${user.asMention} :: ${user.descriptor()}**"
            }
        }
    }

    private fun getLogConfig(guildId: String) = config.getGuildConfig(guildId)!!.loggingChannel
    private fun log(guild: Guild, logChannelId: String, message: MessageEmbed) = logChannelId.takeIf { it.isNotEmpty() }?.idToTextChannel(guild)
            ?.sendMessage(message)?.queue()

    private fun String.idToTextChannel(guild: Guild) = guild.jda.getTextChannelById(this)

}