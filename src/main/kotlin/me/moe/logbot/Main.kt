package me.moe.logbot

import me.aberrantfox.kjdautils.api.dsl.PrefixDeleteMode
import me.aberrantfox.kjdautils.api.startBot
import me.moe.logbot.locale.Messages

fun main(args: Array<String>) {
    val token = args.firstOrNull() ?: return println(Messages().errors.NO_ARGS)

    startBot(token) {
        configure {
            prefix = "log!"
            globalPath = "me.moe.logbot"
            deleteMode = PrefixDeleteMode.None
            allowPrivateMessages = false
        }
    }
}