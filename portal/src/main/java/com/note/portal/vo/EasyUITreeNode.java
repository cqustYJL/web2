package com.note.portal.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author wengyang
 * @Date 2020年02月13日
 **/
public class EasyUITreeNode {
    private String id;
    private String text;
    private String state;
    private Integer catalog_type;
    private String father_id;
    private List<EasyUITreeNode> children = new ArrayList<>();

    public EasyUITreeNode() {
    }

    public EasyUITreeNode(String id, String text, String state, Integer catalog_type, String father_id) {
        this.id = id;
        this.text = text;
        this.state = state;
        this.catalog_type = catalog_type;
        this.father_id = father_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getCatalog_type() {
        return catalog_type;
    }

    public void setCatalog_type(Integer catalog_type) {
        this.catalog_type = catalog_type;
    }

    public String getFather_id() {
        return father_id;
    }

    public void setFather_id(String father_id) {
        this.father_id = father_id;
    }

    public List<EasyUITreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<EasyUITreeNode> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "EasyUITreeNode{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", state='" + state + '\'' +
                ", catalog_type=" + catalog_type +
                ", father_id='" + father_id + '\'' +
                ", children=" + children +
                '}';
    }
}
