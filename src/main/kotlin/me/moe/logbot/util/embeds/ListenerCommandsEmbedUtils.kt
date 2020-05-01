package me.moe.logbot.util.embeds

import me.aberrantfox.kjdautils.api.dsl.embed
import me.moe.logbot.extensions.createContinuableField
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.Role
import java.awt.Color

class ListenerCommandsEmbedUtils {
    companion object {
        fun buildIgnoredRolesEmbed(ignoredRoles: List<String>): MessageEmbed {
            ignoredRoles.map {  }
            return embed {
                title = "Ignored roles"
                description = "These are roles that will not be tracked by the message logger."
                color = infoColor

                createContinuableField("Roles", ignoredRoles.joinToString(separator = "\n"))
            }
        }

        fun buildAlreadyIgnoredEmbed(role: Role): MessageEmbed {
            return embed {
                title = "Role is already ignored"
            }
        }

        fun buildNotIgnoredEmbed(role: Role): MessageEmbed {
            return embed {

            }
        }

        fun buildIgnoredEmbed(role: Role): MessageEmbed {
            return embed {

            }
        }

        fun buildUnignoredEmbed(role: Role): MessageEmbed {
            return embed {

            }
        }
    }
}