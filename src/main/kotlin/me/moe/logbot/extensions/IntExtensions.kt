package me.moe.logbot.extensions

fun Int.toTimeStringFromSec(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val seconds  = this % 60

    return "$hours hour(s), $minutes minute(s) and $seconds second(s)"
}