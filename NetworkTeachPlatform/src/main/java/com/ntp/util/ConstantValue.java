package com.ntp.util;

/**
 * 接口地址
 *
 * @author yanxing
 */
public class ConstantValue {

    public static final String IP = "http://192.168.0.105/ntp/";

    /**
     * 登录网络地址
     */
    public static final String PATH_LOGIN = IP + "phone/login";

    /**
     * 请求课程网络地址
     */
    public static final String PATH_COURSE_LIST = IP + "phone/course-list";

    /**
     * 验证用户名是否存在
     */
    public static final String PATH_USERNAME_EXIST = IP + "phone/user-exist";

    /**
     * 注册地址
     */
    public static final String PATH_REGISTER = IP + "phone/register";

    /**
     * 根据课程代码获取该课程详细信息网络地址
     */
    public static final String PATH_COURSE_DETAIL = IP + "phone/course-detail";

    /**
     * 根据课程代码获取该课程所有课件网络地址
     */
    public static final String PATH_COURSE_WARE = IP + "phone/course-ware";

    /**
     * 请求课程类型网络地址
     */
    public static final String PATH_COURSE_TYPE_LIST = IP + "phone/course-type";

    /**
     * 访问用户服务器端图片地址，需要拼接加上图片名称
     */
    public static String PATH_IMAGE = IP + "upload/";

    /**
     * 访问用户服务器端课件地址，需要拼接加上课件path
     */
    public static String PATH_DOWNLOAD_COURSE_WARE = IP + "upload/";

    /**
     * 访问用户服务器端视频地址，需要拼接加上视频path
     */
    public static String PATH_DOWNLOAD_COURSE_VIDEO = IP + "upload/";

    /**
     * 获取用户详细信息地址
     */
    public static final String PATH_USER_INFO = IP + "phone/user-info";

    /**
     * 用户修改邮箱地址
     */
    public static final String PATH_EMAIL = IP + "phone/modify-email";

    /**
     * 用户修改密码地址
     */
    public static final String PATH_PWD = IP + "phone/modify-pwd";

    /**
     * 用户修改性别地址
     */
    public static final String PATH_SEX = IP + "phone/modify-sex";

    /**
     * 搜索课程路径
     */
    public static final String PATH_COURSE_SEARCH = IP + "phone/course-search";

    /**
     * 搜索我的课程路径
     */
    public static final String PATH_MY_COURSE = IP + "phone/my_course";

    /**
     * 获取我的作业
     */
    public static final String PATH_MY_HOMEWORK = IP + "phone/my_homework";

    /**
     * 根据课程代码获取课程视频
     */
    public static final String PATH_COURSE_VIDEO = IP + "phone/course_video";

    /**
     * 根据课程代码获取课程讨论
     */
    public static final String PATH_COURSE_FORUM = IP + "phone/course_forum";

    /**
     * 根据帖子id获取所有的回复
     */
    public static final String PATH_COURSE_FORUM_ALL = IP + "phone/course_forum_all";

    /**
     * 评论帖子
     */
    public static final String PATH_COURSE_FORUM_REPLY = IP + "phone/course_forum_reply";

    /**
     * 参加课程讨论，发问题
     */
    public static final String PATH_COURSE_FORUM_COMMENT = IP + "phone/course_forum_comment";

    /**
     * 获取作业详情
     */
    public static final String PATH_COURSE_SCORE = IP + "phone/my_score";

    /**
     * 获取回帖
     */
    public static final String PATH_FORUM_COMMENT = IP + "phone/forum_comment_notice";

    /**
     * 课件下载文件夹
     */
    public static final String SAVE_PATH = "mnt/sdcard/ntp/download/";

    /**
     * 上传<UID,CID>
     */
    public static final String PATH_UID_CID = IP + "phone/cid";


    /**
     * 图片缓存文件夹
     */
    public static final String IMAGE_URI = "file:///mnt/sdcard/ntp/";

}
