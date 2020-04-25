package me.moe.logbot.commands

import me.aberrantfox.kjdautils.api.annotation.CommandSet
import me.aberrantfox.kjdautils.api.dsl.command.*
import java.awt.Color
import java.util.Date

private val startTime = Date()

@CommandSet("Utility")
fun utilityCommands() = commands {
    command("Ping") {
        description = "Display the network ping of the bot."
        execute {
            it.respond("Responded in ${it.channel.jda.gatewayPing}ms")
        }
    }

    command("Uptime") {
        description = "Displays how long the bot has been running."
        execute {
            val milliseconds = Date().time - startTime.time
            val seconds = (milliseconds / 1000) % 60
            val minutes = (milliseconds / (1000 * 60)) % 60
            val hours = (milliseconds / (1000 * 60 * 60)) % 24
            val days = (milliseconds / (1000 * 60 * 60 * 24))

            it.respond {
                title = "I have been running since"
                description = startTime.toString()
                color = Color.WHITE

                field {
                    name = "That's been"
                    value = "$days day(s), $hours hour(s), $minutes minute(s) and $seconds second(s)"
                }
            }
        }
    }
}