package com.ntp.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户
 * @author yanxing
 */
public class User {

    private String username;
    private String email;
    private String sex;
    private byte[] head;// 头像

    public User() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public byte[] getHead() {
        return head;
    }

    public void setHead(byte[] head) {
        this.head = head;
    }

    /**
     * @param username 用户名
     * @param head     头像，没有可为空
     * @param sex      性别
     * @param email    邮箱
     */
    public User(String username, byte[] head, String sex, String email) {
        this.username = username;
        this.head = head;
        this.sex = sex;
        this.email = email;
    }

    /**
     * 判断邮箱是否合法
     *
     * @param email 邮箱
     * @return 邮箱格式输入正确返回true，错误返回false
     */
    public static Boolean isEmail(String email) {
        // 匹配邮箱的正则表达时，在java需要进行转义^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$
        // ^ ：匹配输入的开始位置
        // \：将下一个字符标记为特殊字符或字面值
        // * ：匹配前一个字符零次或几次
        // + ：匹配前一个字符一次或多次
        // (pattern) 与模式匹配并记住匹配
        // \w ：与任何单词字符匹配，包括下划线
        // {n,m} 最少匹配 n 次且最多匹配 m 次
        // [a-z] ：表示某个范围内的字符。与指定区间内的任何字符匹配
        // ?：匹配前面的子表达式零次或一次
        // $ ：匹配输入的结尾
        String regex = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
