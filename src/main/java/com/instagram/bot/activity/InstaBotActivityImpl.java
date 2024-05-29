package com.instagram.bot.activity;


import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class InstaBotActivityImpl implements InstaBotActivity {
    @Override
    public void login() {
        log.info("Logging in...");
//        var session = getSession();
//        session.openLoginPage();
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXX");
//        closeSession(session);
    }

    @Override
    public void likeByTags(String... tags) {

    }

    @Override
    public void likeByFeed() {

    }
//
//    private static InstaSession getSession() {
//        int retriesCount = 3;
//        while (true) {
//            try {
//                log.debug("Requesting remote session with retries remaining {}", retriesCount);
//                InstaSession instaSession = Marvin.bootstrapWebApplication(InstaSession.class);
//                instaSession.startSession();
//                return instaSession;
//            } catch (Throwable e) {
//                log.warn("Failed to acquire a running session: {}", e.getMessage());
//                if (retriesCount > 0) {
//                    retriesCount--;
//                } else {
//                    throw e;
//                }
//            }
//        }
//    }
//
//    private void closeSession(InstaSession instaSession) {
//        try {
//            instaSession.closeSession();
//        } catch (Throwable ignored) {
//        }
//    }

}
