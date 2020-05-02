package me.moe.logbot.locale

val messages = Messages()

class Messages(
    val descriptions: Descriptions = Descriptions(),
    val utils: Utils = Utils(),
    val errors: Errors = Errors()
)

class Descriptions (
    val IGNORED_ROLES: String = "View a list of roles currently ignored from message tracking.",
    val IGNORE_ROLE: String = "Add a role to the ignored role list preventing messages from being monitored.",
    val UNIGNORE_ROLE: String = "Remove a role from the ignored role list allowing messages to be monitored.",
    val LISTENER_STATUS: String = "Displays a list of all listeners and whether they are currently enabled or not.",
    val TOGGLE_LISTENER: String = "Toggles a listener on or off."
)

class Errors (

    //Commands
    val GUILD_NOT_SETUP: String = "This guild is not set up for use. Please use the `setup` command.",
    val GUILD_ALREADY_SETUP: String = "This guild has already been setup for use.",
    val COMMAND_OWNER_ONLY: String = "This command must be run by the bot owner.",
    val COMMAND_GUILD_OWNER_ONLY: String = "This command must be run by the guild owner."
)

class Utils (
    val BOT_DESC: String = "A multi-guild discord bot to log everything and everything you could ever want"
)