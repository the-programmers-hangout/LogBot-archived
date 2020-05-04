package me.moe.logbot.extensions

import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.VoiceChannel

fun TextChannel.descriptor() = "**${this.asMention} :: ${this.name} :: ID :: ${this.id}**"
fun VoiceChannel.descriptor() = "**${this.name} :: ID :: ${this.id}**"