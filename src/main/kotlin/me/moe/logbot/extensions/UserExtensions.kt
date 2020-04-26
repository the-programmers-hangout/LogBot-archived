package me.moe.logbot.extensions

import me.aberrantfox.kjdautils.extensions.jda.descriptor
import net.dv8tion.jda.api.entities.User

fun User.verboseDescriptor() = "**${this.asMention} :: ${this.descriptor()}**"