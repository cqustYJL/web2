package com.note.portal.po;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author wengyang
 * @Date 2020年02月12日
 **/

public class Catalog {
    @Id
    private String catalog_id;
    private String catalog_name;
    private Boolean isFolder;
    private String father_id;
    private List<String> children_id = new ArrayList<>();

    public Catalog() {
    }

    public Catalog(String catalog_name, Boolean isFolder, String father_id) {
        this.catalog_name = catalog_name;
        this.isFolder = isFolder;
        this.father_id = father_id;
    }

    public String getCatalog_id() {
        return catalog_id;
    }

    public void setCatalog_id(String catalog_id) {
        this.catalog_id = catalog_id;
    }

    public String getCatalog_name() {
        return catalog_name;
    }

    public void setCatalog_name(String catalog_name) {
        this.catalog_name = catalog_name;
    }

    public Boolean getFolder() {
        return isFolder;
    }

    public void setFolder(Boolean folder) {
        isFolder = folder;
    }

    public String getFather_id() {
        return father_id;
    }

    public void setFather_id(String father_id) {
        this.father_id = father_id;
    }

    public List<String> getChildren_id() {
        return children_id;
    }

    public void setChildren_id(List<String> children_id) {
        this.children_id = children_id;
    }

    @Override
    public String toString() {
        return "Catalog{" +
                "catalog_id=" + catalog_id +
                ", catalog_name='" + catalog_name + '\'' +
                ", isFolder=" + isFolder +
                ", father_id=" + father_id +
                ", children_id=" + children_id +
                '}';
    }
}
