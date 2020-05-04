package me.moe.logbot.extensions

import net.dv8tion.jda.api.entities.Guild


fun Guild.descriptor() = "**${this.name} :: ID :: ${this.id}**"