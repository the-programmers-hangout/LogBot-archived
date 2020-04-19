package me.moe.logbot

import me.aberrantfox.kjdautils.api.startBot

fun main(args: Array<String>) {
    val token = args.firstOrNull() ?: return println("No token provided!")

    startBot(token) {
        configure {
            globalPath = "me.moe.logbot"
        }
    }
}