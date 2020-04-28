package me.moe.logbot.extensions

import net.dv8tion.jda.api.entities.TextChannel

fun TextChannel.descriptor() = "**${this.asMention} :: ${this.name} :: ID :: ${this.id}**"