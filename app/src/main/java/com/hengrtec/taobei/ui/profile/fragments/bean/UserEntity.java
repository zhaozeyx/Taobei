package com.hengrtec.taobei.ui.profile.fragments.bean;

/**
 * 用户数据对象
 * Created by wuxubaiyang on 2016/2/6.
 */
public class UserEntity {
    private String name = "";
    private String sex = "";
    private int age = 0;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}