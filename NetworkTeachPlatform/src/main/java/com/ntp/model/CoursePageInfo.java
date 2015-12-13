package com.ntp.model;

import java.util.List;

/**
 * 课程列表和分页信息 Gson插件GsonFormat自动生成
 * Created by lishuangxiang on 2015/12/13.
 */
public class CoursePageInfo {

    /**
     * currentPage : 1
     * list : [{"code":"a829829","coursetype":{"type":"软件方向"},"image":"C语言.png","name":"C语言","user":{"name":"张三"}},{"code":"a829830","coursetype":{"type":"软件方向"},"image":"C++.png","name":"C++语言编程","user":{"name":"李四"}},{"code":"a829833","coursetype":{"type":"物联网方向"},"image":"面向对象程序设计.png","name":"C#程序设计","user":{"name":"王二"}},{"code":"a829834","coursetype":{"type":"软件方向"},"image":"Java精解案例教程.png","name":"Java精解案例教程","user":{"name":"张三"}},{"code":"a829835","coursetype":{"type":"软件方向"},"image":"软件工程导论.png","name":"软件工程导论","user":{"name":"张三"}},{"code":"a829832","coursetype":{"type":"软件方向"},"image":"数据结构.png","name":"数据结构","user":{"name":"李四"}},{"code":"a829831","coursetype":{"type":"软件方向"},"image":null,"name":"面向对象","user":{"name":"王二"}},{"code":"aaaa","coursetype":{"type":"软件方向"},"image":null,"name":"软件测试","user":{"name":"张三"}},{"code":"C1","coursetype":{"type":"软件方向"},"image":null,"name":"C语言1","user":{"name":"张三"}},{"code":"C2","coursetype":{"type":"软件方向"},"image":null,"name":"C语言2","user":{"name":"张三"}},{"code":"C3","coursetype":{"type":"软件方向"},"image":null,"name":"C语言3","user":{"name":"张三"}},{"code":"C4","coursetype":{"type":"软件方向"},"image":null,"name":"C语言4","user":{"name":"张三"}},{"code":"C5","coursetype":{"type":"软件方向"},"image":null,"name":"C语言5","user":{"name":"张三"}},{"code":"C6","coursetype":{"type":"软件方向"},"image":null,"name":"C语言6","user":{"name":"张三"}},{"code":"C7","coursetype":{"type":"软件方向"},"image":null,"name":"C语言7","user":{"name":"张三"}},{"code":"C8","coursetype":{"type":"软件方向"},"image":null,"name":"C语言8","user":{"name":"张三"}},{"code":"C9","coursetype":{"type":"软件方向"},"image":null,"name":"C语言9","user":{"name":"张三"}},{"code":"C10","coursetype":{"type":"软件方向"},"image":null,"name":"C语言10","user":{"name":"张三"}},{"code":"C11","coursetype":{"type":"软件方向"},"image":null,"name":"C语言11","user":{"name":"张三"}},{"code":"C12","coursetype":{"type":"软件方向"},"image":null,"name":"C语言12","user":{"name":"张三"}}]
     */

    private int currentPage;
    /**
     * code : a829829
     * coursetype : {"type":"软件方向"}
     * image : C语言.png
     * name : C语言
     * user : {"name":"张三"}
     */

    private List<ListEntity> list;

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public static class ListEntity {
        private String code;
        /**
         * type : 软件方向
         */

        private CoursetypeEntity coursetype;
        private String image;
        private String name;
        /**
         * name : 张三
         */

        private UserEntity user;

        public void setCode(String code) {
            this.code = code;
        }

        public void setCoursetype(CoursetypeEntity coursetype) {
            this.coursetype = coursetype;
        }

        public void setImage(String image) {
            if (image.equals("null")){
                this.image="";
            }else {
                this.image = image;
            }
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setUser(UserEntity user) {
            this.user = user;
        }

        public String getCode() {
            return code;
        }

        public CoursetypeEntity getCoursetype() {
            return coursetype;
        }

        public String getImage() {
            return image;
        }

        public String getName() {
            return name;
        }

        public UserEntity getUser() {
            return user;
        }

        public static class CoursetypeEntity {
            private String type;

            public void setType(String type) {
                this.type = type;
            }

            public String getType() {
                return type;
            }
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
