package me.moe.logbot.services

import me.aberrantfox.kjdautils.api.annotation.Service
import me.moe.logbot.data.Configuration
import net.dv8tion.jda.api.entities.Member

enum class Permission {
    BotOwner,
    GuildOwner,
    Administrator,
    Staff,
    Everyone
}

val DEFAULT_REQUIRED_PERMISSION = Permission.Everyone

@Service
class PermissionsService(private val configuration: Configuration) {
    fun hasClearance(member: Member, requiredPermissionLevel: Permission): Boolean {
        return member.getPermissionLevel().ordinal <= requiredPermissionLevel.ordinal
    }
    fun getPermissionLevel(member: Member) = member.getPermissionLevel().ordinal

    private fun Member.getPermissionLevel() =
            when {
                isBotOwner() -> Permission.BotOwner
                isGuildOwner() -> Permission.GuildOwner
                isAdministrator() -> Permission.Administrator
                isStaff() -> Permission.Staff
                else -> Permission.Everyone
            }

    private fun Member.isBotOwner() = user.id == configuration.botOwner
    private fun Member.isGuildOwner() = isOwner
    private fun Member.isAdministrator() : Boolean {
        val guildConfig = configuration.getGuildConfig(this.guild.id)!!

        if(guildConfig.adminRole.isEmpty()) return false

        val requiredRole = guild.getRoleById(guildConfig.adminRole)
                ?: return false

        return requiredRole in roles
    }
    private fun Member.isStaff(): Boolean {
        val guildConfig = configuration.getGuildConfig(this.guild.id)!!

        if(guildConfig.staffRole.isEmpty()) return false

        val requiredRole = guild.getRoleById(guildConfig.staffRole)
                ?: return false

        return requiredRole in roles
    }
}