package com.chzu.ntp.ui;

/**
 * 访问服务器地址，ip经常改变，便于集中更改
 *
 * @author yanxing
 */
public class PathConstant {

    /**
     * 登录网络地址
     */
    public static final String PATH_LOGIN = "http://192.168.1.100/ntp/phone/login";

    /**
     * 请求课程网络地址
     */
    /*public static final String PATH_COURSE_LIST = "http://10.0.2.2/ntp/phone/course-list";*/
    public static final String PATH_COURSE_LIST = "http://192.168.1.100/ntp/phone/course-list";

    /**
     * 验证用户名是否存在
     */
    public static final String PATH_USERNAME_EXIST = "http://192.168.1.100/ntp/phone/user-exist";

    /**
     * 注册地址
     */
    public static final String PATH_REGISTER = "http://192.168.1.100/ntp/phone/register";

    /**
     * 根据课程代码获取该课程详细信息网络地址
     */
    public static final String PATH_COURSE_DETAIL = "http://192.168.1.100/ntp/phone/course-detail";
   /* public static final String PATH = "http://10.0.2.2/ntp/phone/course-detail";*/

    /**
     * 请求课程类型网络地址
     */
    /*public  static final String PATH = "http://10.0.2.2/ntp/phone/course-type";*/
    public static final String PATH_COURSE_TYPE_LIST = "http://192.168.1.100/ntp/phone/course-type";

    /**
     * 访问用户服务器端头像地址，需要拼接加上头像名称
     */
    public static String PATH_HEAD = "http://192.168.1.100/ntp/upload/";

    /**
     * 获取用户详细信息地址
     */
    public static final String PATH_USER_INFO = "http://192.168.1.100/ntp/phone/user-info";

    /**
     * 用户修改邮箱地址
     */
    public static final String PATH_EMAIL = "http://192.168.1.100/ntp/phone/modify-email";

    /**
     * 用户修改密码地址
     */
    public static final String PATH_PWD = "http://192.168.1.100/ntp/phone/modify-pwd";

    /**
     * 用户修改性别地址
     */
    public static final String PATH_SEX = "http://192.168.1.100/ntp/phone/modify-sex";

    /**
     * 搜索课程路径
     */
    /*private static  final String PATH="http://10.0.2.2/ntp/phone/course-search";*/
    public static final String PATH_COURSE_SEARCH = "http://192.168.1.100/ntp/phone/course-search";


}
