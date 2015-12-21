package com.ntp.model.gson;

import java.util.List;

/**
 * 回帖消息
 * Created by lishuangxiang on 2015/12/21.
 */
public class CommentNoticeGson {


    /**
     * currentPage : 1
     * forumUsers : [{"content":"包括","forum":{"content":"同一个静态(static)函数或变量的所有声明都必需包含static存储类型吗？"},"time":"2015-05-05T00:00:00","user":{"head":null,"name":"lyxtime"}},{"content":"不包括","forum":{"content":"同一个静态(static)函数或变量的所有声明都必需包含static存储类型吗？"},"time":"2015-05-05T00:00:00","user":{"head":null,"name":"lyxtime"}},{"content":"不知道","forum":{"content":"同一个静态(static)函数或变量的所有声明都必需包含static存储类型吗？"},"time":"2015-05-05T00:00:00","user":{"head":"http://192.168.0.106/ntp/upload/768900e6-cfcc-434b-b5e3-f72077c67942.jpg","name":"time"}},{"content":"不知道","forum":{"content":"同一个静态(static)函数或变量的所有声明都必需包含static存储类型吗？"},"time":"2015-05-01T00:00:00","user":{"head":"http://192.168.0.106/ntp/upload/768900e6-cfcc-434b-b5e3-f72077c67942.jpg","name":"time"}}]
     */

    private int currentPage;
    /**
     * content : 包括
     * forum : {"content":"同一个静态(static)函数或变量的所有声明都必需包含static存储类型吗？"}
     * time : 2015-05-05T00:00:00
     * user : {"head":null,"name":"lyxtime"}
     */

    private List<ForumUsersEntity> forumUsers;

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setForumUsers(List<ForumUsersEntity> forumUsers) {
        this.forumUsers = forumUsers;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public List<ForumUsersEntity> getForumUsers() {
        return forumUsers;
    }

    public static class ForumUsersEntity {
        private String content;
        /**
         * content : 同一个静态(static)函数或变量的所有声明都必需包含static存储类型吗？
         */

        private ForumEntity forum;
        private String time;
        /**
         * head : null
         * name : lyxtime
         */

        private UserEntity user;

        public void setContent(String content) {
            this.content = content;
        }

        public void setForum(ForumEntity forum) {
            this.forum = forum;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public void setUser(UserEntity user) {
            this.user = user;
        }

        public String getContent() {
            return content;
        }

        public ForumEntity getForum() {
            return forum;
        }

        public String getTime() {
            return time;
        }

        public UserEntity getUser() {
            return user;
        }

        public static class ForumEntity {
            private String content;

            public void setContent(String content) {
                this.content = content;
            }

            public String getContent() {
                return content;
            }
        }

        public static class UserEntity {
            private String head;
            private String name;

            public void setHead(String head) {
                this.head = head;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getHead() {
                return head;
            }

            public String getName() {
                return name;
            }
        }
    }
}
