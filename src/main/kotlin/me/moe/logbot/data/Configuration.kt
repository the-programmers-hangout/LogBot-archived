package me.moe.logbot.data

import me.aberrantfox.kjdautils.api.annotation.Data

data class GuildConfiguration(var guildId: String = "insert_id",
                              var adminRole: String = "insert_role",
                              var staffRole: String = "insert_role",
                              var ignoreRoleNames: MutableList<String> = mutableListOf(),
                              var loggingChannel: String = "insert_id",
                              var historyChannel: String ="insert_id",
                              var trackEmotes: Boolean = true,
                              var trackMembers: Boolean = true,
                              var trackBans: Boolean = true,
                              var trackRoles: Boolean = true,
                              var trackNicknames: Boolean = true,
                              var trackMessages: Boolean = true,
                              var trackReactions: Boolean = true,
                              var trackVoice: Boolean = true,
                              var trackChannels: Boolean = true,
                              var trackCategories: Boolean = true
)

@Data("config/config.json")
data class Configuration(val prefix: String = "++",

                         val botOwner: String = "insert_id",
                         val cacheAmount: Int = 5000,
                         var guildConfigurations: MutableList<GuildConfiguration> = mutableListOf(GuildConfiguration())) {

    fun hasGuildConfig(guildId: String) = getGuildConfig(guildId) != null
    fun getGuildConfig(guildId: String) = guildConfigurations.firstOrNull { it.guildId == guildId }

    fun isTrackingEmotes(guildId: String) = booleanSafety(getGuildConfig(guildId)?.trackEmotes)
    fun isTrackingMembers(guildId: String) = booleanSafety(getGuildConfig(guildId)?.trackMembers)
    fun isTrackingBans(guildId: String) = booleanSafety(getGuildConfig(guildId)?.trackBans)
    fun isTrackingRoles(guildId: String) = booleanSafety(getGuildConfig(guildId)?.trackRoles)
    fun isTrackingNicknames(guildId: String) = booleanSafety(getGuildConfig(guildId)?.trackNicknames)
    fun isTrackingMessages(guildId: String) = booleanSafety(getGuildConfig(guildId)?.trackMessages)
    fun isTrackingReactions(guildId: String) = booleanSafety(getGuildConfig(guildId)?.trackReactions)
    fun isTrackingVoice(guildId: String) = booleanSafety(getGuildConfig(guildId)?.trackVoice)
    fun isTrackingChannels(guildId: String) = booleanSafety(getGuildConfig(guildId)?.trackChannels)
    fun isTrackingCategories(guildId: String) = booleanSafety(getGuildConfig(guildId)?.trackCategories)



    private fun booleanSafety(bool: Boolean?): Boolean {
        return when (bool) {
            true -> true
            false -> false
            else -> false
        }
    }
}
