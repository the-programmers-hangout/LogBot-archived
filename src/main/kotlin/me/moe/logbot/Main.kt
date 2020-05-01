package me.moe.logbot

import me.aberrantfox.kjdautils.api.dsl.PrefixDeleteMode
import me.aberrantfox.kjdautils.api.getInjectionObject
import me.aberrantfox.kjdautils.api.startBot
import me.moe.logbot.data.Configuration
import me.moe.logbot.locale.Messages

fun main(args: Array<String>) {
    val token = args.firstOrNull() ?: return println(Messages().errors.NO_ARGS)

    startBot(token) {
        configure {
            val configuration: Configuration = discord.getInjectionObject<Configuration>()!!

            prefix = configuration.prefix

            allowPrivateMessages = false
            allowMentionPrefix = true
        }
    }
}