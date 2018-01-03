package com.example.user.project2;

import java.util.ArrayList;

public class FacebookUserInfo {

    public static ArrayList<fbContact> fbcontactlist = new ArrayList<>();

    public static class fbContact{
        String img_src = "Default";
        String name = "Default";
    }

    public static ArrayList<fbContact> getfbContactList() {
        return fbcontactlist;
    }
}
