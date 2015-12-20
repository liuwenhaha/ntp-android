package com.ntp.model.gson;

import java.util.List;

/**
 * 问题回复
 * Created by lishuangxiang on 2015/12/20.
 */
public class ForumReplyGson {
    /**
     * currentPage : 1
     * forumUsers : [{"content":"包括","id":22,"time":"2015-05-05T00:00:00","user":{"name":"lyxtime"}},{"content":"不包括","id":23,"time":"2015-05-05T00:00:00","user":{"name":"lyxtime"}},{"content":"不知道","id":24,"time":"2015-05-05T00:00:00","user":{"name":"time"}},{"content":"不知道","id":2,"time":"2015-05-01T00:00:00","user":{"name":"time"}}]
     */

    private int currentPage;
    /**
     * content : 包括
     * id : 22
     * time : 2015-05-05T00:00:00
     * user : {"name":"lyxtime"}
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
        private int id;
        private String time;
        /**
         * name : lyxtime
         */

        private UserEntity user;

        public void setContent(String content) {
            this.content = content;
        }

        public void setId(int id) {
            this.id = id;
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

        public int getId() {
            return id;
        }

        public String getTime() {
            return time;
        }

        public UserEntity getUser() {
            return user;
        }

        public static class UserEntity {
            private String name;

            public void setName(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }
        }
    }
}
