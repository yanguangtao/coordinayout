package com.coordinayout.tgy.myapplication.bean;

import java.io.Serializable;

/**
 * Created by tgy on 2017/2/26.
 */

public class TagBean implements Serializable {
    private int tagId;
    private String tagName;

    public TagBean(int tagId, String tagName) {
        this.tagId = tagId;
        this.tagName = tagName;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
