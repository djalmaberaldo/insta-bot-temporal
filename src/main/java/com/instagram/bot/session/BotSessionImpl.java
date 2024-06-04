package com.instagram.bot.session;

import org.springframework.stereotype.Service;

@Service
public class BotSessionImpl implements  BotSession {

    @Override
    public Session getSession(String url) {
        return new Session(url);
    }
}
