package me.moe.logbot.util

import me.aberrantfox.kjdautils.api.dsl.embed
import net.dv8tion.jda.api.entities.MessageEmbed
import java.awt.Color

class EmbedUtils {
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

                title = "Logger Status"
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