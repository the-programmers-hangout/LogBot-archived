package me.moe.logbot.util.embeds

import me.aberrantfox.kjdautils.api.dsl.embed
import me.moe.logbot.extensions.createContinuableField
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.Role
import java.awt.Color

class ListenerCommandsEmbedUtils {
    companion object {
        fun buildLoggerToggledEmbed(logger: String, active: Boolean): MessageEmbed {
            val colour: Color = if (active) Color.GREEN else Color.RED
            val status: String = if (active) "active" else "not active"

            return embed {
                title = "The logging of $logger is now $status."
                color = colour
            }
        }

        fun buildLoggerStatusEmbed(enabledLoggers: MutableList<String>,
                                   disabledLoggers: MutableList<String>): MessageEmbed {
            return embed {

                title = "Logger status"
                color = Color.ORANGE

                if (enabledLoggers.isNotEmpty())
                    field {
                        name = "Enabled"
                        value = enabledLoggers.joinToString(separator = "\n")
                    }

                if (disabledLoggers.isNotEmpty())
                    field {
                        name = "Disabled"
                        value = disabledLoggers.joinToString(separator = "\n")
                    }
            }
        }
    }
}