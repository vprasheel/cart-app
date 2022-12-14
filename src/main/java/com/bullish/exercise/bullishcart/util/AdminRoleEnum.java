package com.bullish.exercise.bullishcart.util;

public enum AdminRoleEnum {

    REGULAR_USER("regular_user"),
    ADMIN_USER("admin_user");
    private String roleName;
    AdminRoleEnum(String name){
        this.roleName = name;
    }
    public static String fromEnum(AdminRoleEnum roleEnum){
        return roleEnum.roleName;
    }

}
