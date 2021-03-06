package ru.danis0n.getsiteinfobot.model;

public enum BotState {
    START, // starts the session
    HELP, // the bot will show all commands to user
    SHOWTOPANIME, // the bot will show top-10 anime titles
    SHOWGENRES, // the bot will show all genres
    SHOWALLUSERS, // the bot will show all users (admin only)
    SHOWABOUTAUTHOR, // the bot will show information about author and GitHub link
    SHOWONGOINGS, // the bot will show information about popular ongoings with links
    ENTERNUMBERTITLEFORMORE, // the bot will wait for number to be entered
    PREPAREONGOINGS, // the bot will load from site all popular ongoings
    SHOWANIME, // the bot will show title
    SHOWGITHUB, // the bot will show GitHub link
    ENTERNUMBERUSER, // the bot will wait for user to be entered (to delete)
    ENTERNUMBERGENRE, // the bot will wait for user to enter number/s of genres to scan for titles
}