package com.ntp.model.gson;

import java.util.List;

/**
 * 课程问题
 * Created by lishuangxiang on 2015/12/16.
 */
public class CourseForumGson {


    /**
     * currentPage : 1
     * forums : [{"content":"同一个静态(static)函数或变量的所有声明都必需包含static存储类型吗？","id":21,"replyNumber":4,"time":"2015-05-04T00:00:00","user":{"head":"http://192.168.0.105/ntp/upload/75110c11-c2db-42fe-8d1c-f7c1bfa3e3f1.jpg","name":"yanxing"}},{"content":"我似乎不能成功定义一个链表。我试过typedefstruct{char*item;NODEPTRnext;}*NODEPTR;但是编译器报了错误信息。难道在C语言中结构不能包含指向自己的指针吗？","id":22,"replyNumber":0,"time":"2015-05-04T00:00:00","user":{"head":"http://192.168.0.105/ntp/upload/768900e6-cfcc-434b-b5e3-f72077c67942.jpg","name":"time"}},{"content":"C语言难吗","id":23,"replyNumber":null,"time":"2015-05-04T00:00:00","user":{"head":"http://192.168.0.105/ntp/upload/768900e6-cfcc-434b-b5e3-f72077c67942.jpg","name":"time"}},{"content":"C语言难吗1","id":24,"replyNumber":null,"time":"2015-05-04T00:00:00","user":{"head":"http://192.168.0.105/ntp/upload/75110c11-c2db-42fe-8d1c-f7c1bfa3e3f1.jpg","name":"yanxing"}},{"content":"C语言难吗2","id":25,"replyNumber":null,"time":"2015-05-04T00:00:00","user":{"head":"http://192.168.0.105/ntp/upload/75110c11-c2db-42fe-8d1c-f7c1bfa3e3f1.jpg","name":"yanxing"}},{"content":"C语言难吗3","id":26,"replyNumber":null,"time":"2015-05-04T00:00:00","user":{"head":"http://192.168.0.105/ntp/upload/75110c11-c2db-42fe-8d1c-f7c1bfa3e3f1.jpg","name":"yanxing"}},{"content":"C语言难吗4","id":27,"replyNumber":null,"time":"2015-05-04T00:00:00","user":{"head":"http://192.168.0.105/ntp/upload/75110c11-c2db-42fe-8d1c-f7c1bfa3e3f1.jpg","name":"yanxing"}},{"content":"C语言难吗5","id":28,"replyNumber":null,"time":"2015-05-04T00:00:00","user":{"head":"http://192.168.0.105/ntp/upload/75110c11-c2db-42fe-8d1c-f7c1bfa3e3f1.jpg","name":"yanxing"}},{"content":"C语言难吗6","id":29,"replyNumber":null,"time":"2015-05-04T00:00:00","user":{"head":"http://192.168.0.105/ntp/upload/75110c11-c2db-42fe-8d1c-f7c1bfa3e3f1.jpg","name":"yanxing"}},{"content":"C语言难吗7","id":30,"replyNumber":null,"time":"2015-05-04T00:00:00","user":{"head":"http://192.168.0.105/ntp/upload/75110c11-c2db-42fe-8d1c-f7c1bfa3e3f1.jpg","name":"yanxing"}}]
     */

    private int currentPage;
    /**
     * content : 同一个静态(static)函数或变量的所有声明都必需包含static存储类型吗？
     * id : 21
     * replyNumber : 4
     * time : 2015-05-04T00:00:00
     * user : {"head":"http://192.168.0.105/ntp/upload/75110c11-c2db-42fe-8d1c-f7c1bfa3e3f1.jpg","name":"yanxing"}
     */

    private List<ForumsEntity> forums;

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setForums(List<ForumsEntity> forums) {
        this.forums = forums;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public List<ForumsEntity> getForums() {
        return forums;
    }

    public static class ForumsEntity {
        private String content;
        private int id;
        private int replyNumber;
        private String time;
        /**
         * head : http://192.168.0.105/ntp/upload/75110c11-c2db-42fe-8d1c-f7c1bfa3e3f1.jpg
         * name : yanxing
         */

        private UserEntity user;

        public void setContent(String content) {
            this.content = content;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setReplyNumber(int replyNumber) {
            this.replyNumber = replyNumber;
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

        public int getReplyNumber() {
            return replyNumber;
        }

        public String getTime() {
            return time;
        }

        public UserEntity getUser() {
            return user;
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
