package me.moe.logbot

import me.aberrantfox.kjdautils.api.getInjectionObject
import me.aberrantfox.kjdautils.api.startBot
import me.moe.logbot.data.Configuration

fun main(args: Array<String>) {
    val token = args.firstOrNull() ?: return println("No program arguments provided. Expected bot token.")

    startBot(token) { }
}