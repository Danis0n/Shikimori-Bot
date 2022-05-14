package ru.danis0n.getsiteinfobot.model;

public enum BotState {
    START, // starts the session
    HELP, // the bot will show all commands to user
    SHOWALLUSERS, // the bot will show all users (admin only)
    SHOWABOUTAUTHOR, // the bot will show information about author and GitHub link
    SHOWPOPULARONGOINGS, // the bot will show information about popular ongoings with links
    ENTERNUMBERTITLEFORMORE, // the bot will wait for number to be entered
    PREPAREONGOINGS, // the bot will load from site all popular ongoings
    SHOWANIME, // the bot will show title
    ENTERNUMBERUSER, // the boy will wait for user to be entered (to delete)
}
