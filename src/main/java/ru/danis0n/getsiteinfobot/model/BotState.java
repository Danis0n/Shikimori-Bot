package ru.danis0n.getsiteinfobot.model;

public enum BotState {
    START, // starts the session
    HELP, // the bot will show all commands to user
    SHOWALLUSERS, // the bot will show all users (admin only)
    SHOWABOUTAUTHOR, // the bot will show information about author and GitHub link
    SHOWPOPULARONGOINGS, // the bot will show information about popular ongoings with links
    SHOWANIME, // the bot will show title
}
