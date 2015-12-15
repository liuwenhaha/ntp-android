package com.ntp.model.gson;

import java.util.List;

/**
 * 课程类型 GsonFormat自动生成
 * Created by lishuangxiang on 2015/12/13.
 */
public class CourseTypeGson {

    /**
     * type : 软件方向
     */

    private List<ListCTypeEntity> listCType;

    public void setListCType(List<ListCTypeEntity> listCType) {
        this.listCType = listCType;
    }

    public List<ListCTypeEntity> getListCType() {
        return listCType;
    }

    public static class ListCTypeEntity {
        private String type;

        public void setType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }
}
