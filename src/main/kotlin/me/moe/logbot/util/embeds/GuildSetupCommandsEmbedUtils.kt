package me.moe.logbot.util.embeds

import me.aberrantfox.kjdautils.api.dsl.embed
import me.moe.logbot.extensions.createContinuableField
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
    }
}