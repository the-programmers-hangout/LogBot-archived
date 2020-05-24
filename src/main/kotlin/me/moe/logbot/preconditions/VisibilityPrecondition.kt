package me.moe.logbot.preconditions

import me.aberrantfox.kjdautils.api.annotation.Precondition
import me.aberrantfox.kjdautils.extensions.jda.toMember
import me.aberrantfox.kjdautils.internal.command.Fail
import me.aberrantfox.kjdautils.internal.command.Pass
import me.aberrantfox.kjdautils.internal.command.precondition
import me.moe.logbot.data.Configuration

@Precondition
fun produceCanSeePrecondition(configuration: Configuration) = precondition {

    // if in dms
    it.guild ?: return@precondition Fail()

    val config = configuration.getGuildConfig(it.guild!!.id) ?: return@precondition Fail()

    val staffRoles = it.author.toMember(it.guild!!)!!.roles.filter { role ->
        role.id == config.adminRole || role.id == config.staffRole
    }

    if (staffRoles.isEmpty()) {
        return@precondition Fail()
    }

    return@precondition Pass
}