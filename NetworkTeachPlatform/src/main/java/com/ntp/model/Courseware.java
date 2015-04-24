package com.ntp.model;

/**
 * @author yanxing
 * 课件对象，为listview上的一个item
 */
public class Courseware {

    private String id;
    private String name;
    private String size;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    /**
     * @param id   课件id
     * @param name 课件名称
     * @param size 课件大小
     */
    public Courseware(String id,  String name,String size) {
        this.id = id;
        this.size = size;
        this.name = name;
    }
}
