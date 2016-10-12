package com.tys.ms.model;

public enum UserProfileType {
    REGULAR("REGULAR"),
    GROUP("GROUP"),
    AREA("AREA"),
    ADMIN("ADMIN");

    String userProfileType;

    private UserProfileType(String userProfileType){
        this.userProfileType = userProfileType;
    }

    public String getUserProfileType(){
        return userProfileType;
    }
}
