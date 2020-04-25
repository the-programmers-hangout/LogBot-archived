package me.moe.logbot.locale

val messages = Messages()

class Messages(
    val descriptions: Descriptions = Descriptions(),
    val errors: Errors = Errors()
)

class Descriptions(

)

class Errors(
    //General
    val NO_ARGS: String = "No program arguments provided. Expected bot token.",

    //Commands
    val GUILD_NOT_SETUP: String = "This guild is not set up for use. Please use the `setup` command.",
    val GUILD_ALREADY_SETUP: String = "This guild is already setup for use.",
    val COMMAND_OWNER_ONLY: String = "This command must be run by the bot owner."
)