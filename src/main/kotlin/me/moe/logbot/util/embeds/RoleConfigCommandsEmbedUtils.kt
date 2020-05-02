package me.moe.logbot.util.embeds

import me.aberrantfox.kjdautils.api.dsl.embed
import me.moe.logbot.extensions.createContinuableField
import me.moe.logbot.extensions.descriptor
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.Role
import java.awt.Color

class RoleConfigCommandsEmbedUtils {
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
                field {
                    name = "Role"
                    value = role.descriptor()
                }
                color = failureColor
            }
        }

        fun buildNotIgnoredEmbed(role: Role): MessageEmbed {
            return embed {
                title = "Role is not ignored"
                field {
                    name = "Role"
                    value = role.descriptor()
                }
                color = failureColor
            }
        }

        fun buildIgnoredEmbed(role: Role): MessageEmbed {
            return embed {
                title = "Role added to the ignore list"
                field {
                    name = "Role"
                    value = role.descriptor()
                }
                color = successColor
            }
        }

        fun buildUnignoredEmbed(role: Role): MessageEmbed {
            return embed {
                title = "Role removed from the ignore list"
                field {
                    name = "Role"
                    value = role.descriptor()
                }
                color = successColor
            }
        }
    }
}