package com.instagram.bot.activity;


import org.springframework.stereotype.Component;

@Component
public class InstaBotActivityImpl implements InstaBotActivity {
    @Override
    public void login() {
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXX");
    }

    @Override
    public void likeByTags(String... tags) {

    }

    @Override
    public void likeByFeed() {

    }

}
