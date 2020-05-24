package me.moe.logbot.locale

val messages = Messages()

class Messages(
    val descriptions: Descriptions = Descriptions(),
    val utils: Utils = Utils(),
    val errors: Errors = Errors()
)

class Descriptions (
        // Guild config
    val ADMIN_ROLE: String = "Sets the admin role.",
    val HISTORY_CHANNEL: String = "Sets the channel where message history is logged.",
    val LOGGING_CHANNEL: String = "Sets the channel where events are logged.",
    val STAFF_ROLE: String = "Sets the staff role.",
    val VIEW_CONFIGURATION: String = "View configuration values",

    // Role Config
    val IGNORED_ROLES: String = "View a list of roles currently ignored from message tracking.",
    val IGNORE_ROLE: String = "Add a role to the ignored role list preventing messages from being monitored.",
    val UNIGNORE_ROLE: String = "Remove a role from the ignored role list allowing messages to be monitored.",

    // Listeners
    val LISTENER_STATUS: String = "Displays a list of all listeners and whether they are currently enabled or not.",
    val TOGGLE_LISTENER: String = "Toggles a listener on or off."
)

class Errors (

    //Commands
    val GUILD_NOT_SETUP: String = "This guild is not set up for use. Please use the `setup` command."
)

class Utils (
    val BOT_DESC: String = "A multi-guild discord bot to log everything and everything you could ever want"
)