package com.instagram.bot.session;

public interface BotSession {

    enum Browser {
        FIREFOX, CHROME
    }

    Session getSession(String url);
}
