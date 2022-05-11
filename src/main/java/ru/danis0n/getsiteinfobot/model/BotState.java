package ru.danis0n.getsiteinfobot.model;

public enum BotState {
    START, // starts the session
    ALLUSERS, // the bot will show all users (admin only)
    ALLCHARACTERS, // the bot will show all characters
    ALLCRYO, // the bot will show all cryo characters
    ALLPYRO, // the bot will show all pyro characters
    ALLHYDRO, // the bot will show all hydro characters
    ALLELECTRO, // the bot will show all electro characters
    ALLGEO, // the bot will show all geo characters
    ALLMAINDPS, // the bot will show all best-maindps characters
    ALLSUBDPS, // the bot will show all best-subdps characters
    ALLUTILITY, // the bot will show all best-utility characters
    ABOUTAUTHOR, // the bot will show information about author and github link
}
