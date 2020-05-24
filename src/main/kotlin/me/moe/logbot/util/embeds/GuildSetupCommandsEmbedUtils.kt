package me.moe.logbot.util.embeds

import me.aberrantfox.kjdautils.api.dsl.embed
import me.aberrantfox.kjdautils.extensions.jda.getRoleByName
import me.moe.logbot.extensions.createContinuableField
import me.moe.logbot.extensions.descriptor
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.Role
import java.awt.Color

class GuildSetupCommandsEmbedUtils {
    companion object {
        fun buildGuildSetupEmbed(guild: Guild): MessageEmbed {
            return embed {
                title = "Configuration for: __**${guild.name}**__"
                thumbnail = guild.iconUrl
            }
        }

        fun buildGuildConfigEmbed(guild: String, adminRole: String, staffRole: String, loggingChannel: String,
                                  historyChannel: String, cacheAmt: Int): MessageEmbed {
            return embed {
                title = "Guild Config"
                color = infoColor

                field {
                    name = "Guild"
                    value = guild
                }

                field {
                    name = "Admin Role"
                    value = adminRole
                }

                field {
                    name = "Staff Role"
                    value = staffRole
                }

                field {
                    name = "Logging Channel"
                    value = loggingChannel
                }

                field {
                    name = "History Channel"
                    value = historyChannel
                }

                field {
                    name = "Message Cache Amount"
                    value = cacheAmt.toString()
                }

            }
        }
    }
}