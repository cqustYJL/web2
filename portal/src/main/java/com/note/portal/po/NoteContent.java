package com.note.portal.po;

import org.springframework.data.annotation.Id;

/**
 * @Description:
 * @Author wengyang
 * @Date 2020年02月16日
 **/
public class NoteContent {
    @Id
    private String catalog_id;
    private String content;

    public NoteContent() {
    }

    public NoteContent(String catalog_id, String content) {
        this.catalog_id = catalog_id;
        this.content = content;
    }

    public String getCatalog_id() {
        return catalog_id;
    }

    public void setCatalog_id(String catalog_id) {
        this.catalog_id = catalog_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "NoteContent{" +
                "catalog_id='" + catalog_id + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
