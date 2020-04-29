package me.moe.logbot.extensions

import me.aberrantfox.kjdautils.api.dsl.command.Command
import me.moe.logbot.services.DEFAULT_REQUIRED_PERMISSION
import me.moe.logbot.services.Permission

val commandPermissions: MutableMap<Command, Permission> = mutableMapOf()

var Command.requiredPermissionLevel: Permission
    get() = commandPermissions[this] ?: DEFAULT_REQUIRED_PERMISSION
    set(value) {
        commandPermissions[this] = value
    }