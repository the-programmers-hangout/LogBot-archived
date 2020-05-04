package me.moe.logbot.util.embeds

import me.aberrantfox.kjdautils.api.dsl.embed
import me.moe.logbot.locale.messages
import net.dv8tion.jda.api.entities.MessageEmbed

class UtilEmbeds {
    companion object {
        fun buildErrorEmbed(desc: String): MessageEmbed {
            return embed {
                title = desc
                color = failureColor
            }
        }

        fun buildSuccessEmbed(desc: String): MessageEmbed {
            return embed {
                title = desc
                color = successColor
            }
        }

        fun buildGuildNotSetupEmbed(): MessageEmbed {
            return embed {
                title = messages.errors.GUILD_NOT_SETUP
                color = failureColor
            }
        }

    }
}