package me.moe.logbot.data

import me.aberrantfox.kjdautils.api.annotation.Data

data class GuildConfiguration(var guildId: String = "insert_id",
                              var ignoreRoleNames: List<String> = listOf("Staff", "Active"),
                              var staffRoleName: String = "Admin",
                              var loggingChannel: String = "insert_id",
                              var historyChannel: String ="insert_id",
                              // the latest 4000 messages will be tracked for edits/deletions
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
