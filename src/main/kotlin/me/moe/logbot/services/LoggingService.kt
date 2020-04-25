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
    fun buildEmoteLoggerEmbed(guild: Guild, emote: Emote, created: Boolean) = withLog(guild) {
        embed {
            title = "Emoji ${if (created) "added" else "removed"}"
            description = "The emote `${emote.name}` has been ${if (created) "added" else "removed"}"
            color = if (created) Color.GREEN else Color.RED
            image = emote.imageUrl
            footer {
                text = LocalDateTime.now().format(dateTimeFormatter)
            }
        }
    }

    fun buildEmoteRenameEmbed(guild: Guild, event: EmoteUpdateNameEvent) = withLog(guild) {
        embed {
            title = "Emote rename"
            description = "The emote `${event.oldName}` has been renamed to `${event.newName}`"
            color = Color.WHITE
            image = event.emote.imageUrl
            footer {
                text = LocalDateTime.now().format(dateTimeFormatter)
            }
        }
    }

    /*

        Joins / Leaves

     */

    fun buildMemberLoggerEmbed(guild: Guild, user: User, join: Boolean) = withLog(guild) {
        embed {
            title = "User ${if (join) "join" else "leave"}"
            description = "${user.asMention} :: ${user.descriptor()}\n" +
                    "created at " + "${user.timeCreated.format(dateTimeFormatter)} " +
                    "${if (join) "joined" else "left"} the server"

            color = if (join) Color.GREEN else Color.RED
            author {
                name = user.name + if (join) " joined" else " left"
                iconUrl = user.avatarUrl
            }
            footer {
                text = LocalDateTime.now().format(dateTimeFormatter)
            }
        }
    }


    private fun getLogConfig(guildId: String) = config.getGuildConfig(guildId)!!.loggingChannel
    private fun log(guild: Guild, logChannelId: String, message: MessageEmbed) = logChannelId.takeIf { it.isNotEmpty() }?.idToTextChannel(guild)
            ?.sendMessage(message)?.queue()

    private fun String.idToTextChannel(guild: Guild) = guild.jda.getTextChannelById(this)

}