package me.moe.logbot.data

import me.aberrantfox.kjdautils.api.annotation.Data

data class GuildConfiguration(var guildId: String = "insert_id",
                              var adminRole: String = "insert_role",
                              var staffRole: String = "insert_role",
                              var ignoreRoleNames: MutableList<String> = mutableListOf("Staff", "Active"),
                              var loggingChannel: String = "insert_id",
                              var historyChannel: String ="insert_id",
                              var messageCacheAmount: Int = 4000,
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
                         val maxCacheAmount: Int = 5000,
                         var guildConfigurations: MutableList<GuildConfiguration> = mutableListOf(GuildConfiguration())) {

    fun hasGuildConfig(guildId: String) = getGuildConfig(guildId) != null
    fun getGuildConfig(guildId: String) = guildConfigurations.firstOrNull { it.guildId == guildId }
}
