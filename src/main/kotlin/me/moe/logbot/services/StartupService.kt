package me.moe.logbot.services

import com.google.gson.Gson
import me.aberrantfox.kjdautils.api.annotation.Service
import me.aberrantfox.kjdautils.discord.Discord
import me.aberrantfox.kjdautils.extensions.jda.fullName
import me.moe.logbot.data.Configuration
import me.moe.logbot.locale.Messages
import java.awt.Color
import java.util.*

data class Properties(val author: String, val version: String, val kutils: String, val repository: String)

private val propFile = Properties::class.java.getResource("/properties.json").readText()
val project: Properties = Gson().fromJson(propFile, Properties::class.java)

private val startTime = Date()

@Service
class StartupService(configuration: Configuration,
                     discord: Discord) {
    init {
        with(discord.configuration) {
            colors {
                successColor = Color(0x81A275)
                failureColor = Color(0xB14552)
                infoColor = Color(0xF4F4F4)
            }
            
            mentionEmbed {


                with(project) {
                    val self = it.guild.jda.selfUser
                    val kotlinVersion = KotlinVersion.CURRENT
                    val milliseconds = Date().time - startTime.time
                    val seconds = (milliseconds / 1000) % 60
                    val minutes = (milliseconds / (1000 * 60)) % 60
                    val hours = (milliseconds / (1000 * 60 * 60)) % 24
                    val days = (milliseconds / (1000 * 60 * 60 * 24))

                    color = infoColor
                    thumbnail = self.effectiveAvatarUrl

                    addField(self.fullName(), Messages().utils.BOT_DESC)

                    addInlineField("Prefix", configuration.prefix)
                    addInlineField("Ping", "${discord.jda.gatewayPing}ms")
                    addField("Uptime", "$days day(s), " +
                            "$hours hour(s), " +
                            "$minutes minute(s) " +
                            "and $seconds second(s)")

                    addField("Build Info", "```" +
                            "Version: $version\n" +
                            "KUtils: $kutils\n" +
                            "Kotlin: $kotlinVersion" +
                            "```")



                    addField("Source", repository)

                }

            }
        }
    }
}