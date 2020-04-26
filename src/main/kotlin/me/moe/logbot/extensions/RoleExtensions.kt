package me.moe.logbot.extensions

import net.dv8tion.jda.api.entities.Role

fun Role.descriptor() = "**${this.asMention} :: ${this.name} :: ID :: ${this.id}**"