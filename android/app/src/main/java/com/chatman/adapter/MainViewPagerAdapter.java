package com.chatman.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.chatman.fragment.ChatBot;
import com.chatman.fragment.ChatList;

public class MainViewPagerAdapter extends FragmentStatePagerAdapter {
    private Context context;
    public MainViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                ChatList chatList = new ChatList();
                chatList.setContext(context);
                return chatList;
            case 1:
                return new ChatBot();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        super.getPageTitle(position);
        switch (position) {
            case 0:
                return "Chats";
            case 1:
                return "Bot";
            default:
                return null;
        }
    }
}
