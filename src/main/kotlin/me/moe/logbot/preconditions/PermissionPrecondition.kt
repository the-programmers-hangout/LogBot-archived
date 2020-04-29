package me.moe.logbot.preconditions

import me.aberrantfox.kjdautils.api.annotation.Precondition
import me.aberrantfox.kjdautils.extensions.jda.toMember
import me.aberrantfox.kjdautils.internal.command.Fail
import me.aberrantfox.kjdautils.internal.command.Pass
import me.aberrantfox.kjdautils.internal.command.precondition
import me.moe.logbot.extensions.requiredPermissionLevel
import me.moe.logbot.locale.messages
import me.moe.logbot.services.Permission
import me.moe.logbot.services.PermissionsService

@Precondition
fun produceHasPermissionPrecondition(permissionsService: PermissionsService) = precondition {
    val command = it.container[it.commandStruct.commandName] ?: return@precondition Fail()
    val requiredPermissionLevel = command.requiredPermissionLevel
    val guild = it.guild!!
    val member = it.author.toMember(guild)!!

    val response = when (requiredPermissionLevel) {
        Permission.BotOwner -> " You must be the bot owner."
        Permission.GuildOwner -> " You must be the guild owner."
        else -> ""
    }

    if (!permissionsService.hasClearance(member, requiredPermissionLevel))
        return@precondition Fail(response)

    return@precondition Pass
}