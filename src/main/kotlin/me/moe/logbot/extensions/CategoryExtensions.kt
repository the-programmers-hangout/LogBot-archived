package me.moe.logbot.extensions

import net.dv8tion.jda.api.entities.Category
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.VoiceChannel

fun Category.descriptor() = "**${this.name} :: ID :: ${this.id}**"