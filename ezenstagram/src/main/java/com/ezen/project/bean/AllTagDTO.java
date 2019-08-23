package com.ezen.project.bean;


public class AllTagDTO {
    
    private int tag_id; //태그번호
    private String tag_name;    //태그내용
    
    
    public AllTagDTO() {
        super();
    }


    public AllTagDTO(int tag_id, String tag_name) {
        super();
        this.tag_id = tag_id;
        this.tag_name = tag_name;
    }


    public int getTag_id() {
        return tag_id;
    }


    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }


    public String getTag_name() {
        return tag_name;
    }


    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    
    
}
